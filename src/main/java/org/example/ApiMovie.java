package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiMovie {
    final private int MAX_CHUNK  = 10;

    final public String image_url = "https://image.tmdb.org/t/p/original/";

    final public String url = "https://api.themoviedb.org/3/movie/popular?api_key=ffce041fd29b69ec51a364039aca4d4a&language=en-US&";//page=1
    URI address  = URI.create(url);
    HttpClient client  = HttpClient.newHttpClient();
    private List<Movie> movies = new ArrayList<Movie>();;
    private ObjectMapper objectMapper = new ObjectMapper();
    private int totalPages;

    public boolean success;

    public String errorMessage;
    ApiMovie(){
        try{
            String bodyJson;
            HttpRequest request = HttpRequest.newBuilder(address).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            bodyJson = response.body();
            JsonNode node = objectMapper.readTree(bodyJson);
            totalPages = node.get("total_pages").asInt();
            loadData();
            success = true;
        }catch (Exception e){
            errorMessage = e.getMessage();
            success = false;
        }
    }


    private void loadChunk(String urlChunk) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder(new URI(urlChunk)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String bodyJson;
        bodyJson = response.body();
        JsonNode node = objectMapper.readTree(bodyJson);
        JsonNode results = node.get("results");
        if (results.isArray()){
            for (final JsonNode movie : results) {
                Movie newMovie = objectMapper.treeToValue(movie, Movie.class);
                newMovie.poster_path = image_url + newMovie.poster_path;
                newMovie.backdrop_path = image_url + newMovie.backdrop_path;
                movies.add(newMovie);
            }
        }
    }
    private void loadData() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("Loading data.....");
        for (int i = 1; i < totalPages && i < MAX_CHUNK; i++){
            String urlChunk = url + "page=" + i;
            loadChunk(urlChunk);
        }
    }

    ///GETTERS
    public List<Movie> getMovies() {
        return movies;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
