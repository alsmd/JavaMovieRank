package org.doc;

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

public class ApiMovie extends DefaultApi{
    // CONSTANTS
    final private int MAX_CHUNK  = 10;
    final public String image_url = "https://image.tmdb.org/t/p/original/";
    final public String url = "https://api.themoviedb.org/3/movie/popular?api_key=ffce041fd29b69ec51a364039aca4d4a&language=en-US&";//page=1
    final private ObjectMapper objectMapper = new ObjectMapper();

    // VARIABLES
    private List<Movie> movies = new ArrayList<Movie>();;
    private List<RankerData> moviesRankerData = new ArrayList<RankerData>();;
    private int totalPages;

    ApiMovie(){
        try{
            this.createClient(this.url);
            String bodyJson = this.simleRequest();
            JsonNode node = objectMapper.readTree(bodyJson);
            this.totalPages = node.get("total_pages").asInt();
            this.loadData();
            this.success = true;
        }catch (Exception e){
            this.errorMessage = e.getMessage();
            this.success = false;
        }
    }

    /**
     * @brief Get data from urlChunk and add to movies's and moviesRankerData's list
     * @param urlChunk  Url where this chunk of data is gonna come
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    private void loadChunk(String urlChunk) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder(new URI(urlChunk)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String bodyJson;
        bodyJson = response.body();
        JsonNode node = objectMapper.readTree(bodyJson);
        JsonNode results = node.get("results");
        if (results.isArray()){
            for (final JsonNode movie : results) {
                ApiMovie.Movie newMovie = objectMapper.treeToValue(movie, ApiMovie.Movie.class);
                newMovie.poster_path = image_url + newMovie.poster_path;
                newMovie.backdrop_path = image_url + newMovie.backdrop_path;
                newMovie.initData();
                movies.add(newMovie);
                moviesRankerData.add(newMovie);
            }
        }

    }

    /**
     * @brief   Load Data from api
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    private void loadData() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("Loading data.....");
        for (int i = 1; i < totalPages && i < MAX_CHUNK; i++){
            String urlChunk = url + "page=" + i;
            loadChunk(urlChunk);
        }
    }

    ///GETTERS

    /**
     * @return All Data retrevied from api in a structure where ImageRanker class can use
     */
    @Override
    public List<RankerData> getData() {
        return this.moviesRankerData;
    }


    // Classes

    static public class Movie extends  RankerData {
        public String poster_path;
        public boolean adult;
        public String overview;
        public Number vote_count;
        public Number vote_average;
        public String title;
        public String original_title;
        public String release_date;
        public List<Number> genre_ids;
        public int id;
        public String backdrop_path;
        public String original_language;
        public int popularity;
        public boolean video;

        Movie() {

        }
        @Override
        void initData(){
            this.name = this.title;
            this.imageUrl = this.poster_path;
            this.vote = this.vote_average;
        }
    }
}
