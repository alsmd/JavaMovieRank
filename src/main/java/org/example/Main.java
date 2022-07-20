package org.example;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.*;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ApiMovie api = new ApiMovie();
        if (api.success == false){
            System.out.println("Something went wrong with the api! Log: ");
            System.out.println(api.getErrorMessage());
            return ;
        }
        try {
            MovieRanker ranker = new MovieRanker();
            System.out.println("Ranking Movies....");
            for (Movie movie : api.getMovies()){
                ranker.saveImage(movie);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}