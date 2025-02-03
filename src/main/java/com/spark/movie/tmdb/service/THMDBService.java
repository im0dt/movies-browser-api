package com.spark.movie.tmdb.service;

import com.spark.movie.model.CastImage;
import com.spark.movie.model.MovieImage;
import com.spark.movie.tmdb.model.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class THMDBService {
    private final WebClient webClient;

    public THMDBService(){
        this.webClient = WebClient.builder().baseUrl("https://api.themoviedb.org/3")//this.picsProperties.getApiUrl())
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE,
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private THMDBMovie findMovie(String imdbId){
        THMDBFindResponse response = new THMDBFindResponse();
        try {
            response = webClient.get()
                    .uri( "/find/tt" + imdbId + "?external_source=imdb_id&api_key=" + "15d2ea6d0dc1d476efbca3eba2b9bbfb")
                    .retrieve()
                    .toEntity(THMDBFindResponse.class)
                    .toFuture()
                    .get()
                    .getBody();
            return response.getMovie_results().get(0);
        } catch (InterruptedException e) {
            return new THMDBMovie();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MovieImage> extractMovieImage(String imdbId) {
        List<MovieImage> result = new ArrayList<>();
        THMDBMovie movie = findMovie(imdbId);
        THMDBImageResponse response = new THMDBImageResponse();
        try {
            response = webClient.get()
                    .uri("/movie/" + movie.getId() + "/images?api_key=" + "15d2ea6d0dc1d476efbca3eba2b9bbfb")
                    .retrieve()
                    .toEntity(THMDBImageResponse.class)
                    .toFuture()
                    .get().getBody();
        } catch (InterruptedException e) {
            return result;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        result.addAll(response.getBackdrops().stream()
                .map(x-> new MovieImage(x.getFile_path(), "backdrop", "thmoviedb", 0, 0)).toList());
        result.addAll(response.getPosters().stream()
                .map(x-> new MovieImage(x.getFile_path(), "poster", "thmoviedb", 0, 0)).toList());
        return result;
    }

    public List<MovieImage> extractMovieVideo(String imdbId){
        List<MovieImage> result = new ArrayList<>();
        THMDBMovie movie = findMovie(imdbId);
        THMDBVideoResponse response = new THMDBVideoResponse();
        try {
            response = webClient.get()
                    .uri( "/movie/" + movie.getId() + "/videos?api_key=" + "15d2ea6d0dc1d476efbca3eba2b9bbfb")
                    .retrieve()
                    .toEntity(THMDBVideoResponse.class)
                    .toFuture()
                    .get().getBody();
            for (THMDBVideo thmdbVideo : response.getResults()) {
                    MovieImage mi = new MovieImage();
                    mi.setType("movie");
                    mi.setSource("thmoviedb");
                    mi.setUrl(thmdbVideo.getKey());
                    result.add(mi);
            }
        } catch (InterruptedException e) {
            return result;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private List<THMDBPerson> findCast(String name){
        THMDBFindPersonResponse response = new THMDBFindPersonResponse();
        try {
            response = webClient.get()
                    .uri( "/search/person?query=" + name + "&include_adult=true&api_key=" + "15d2ea6d0dc1d476efbca3eba2b9bbfb")
                    .retrieve()
                    .toEntity(THMDBFindPersonResponse.class)
                    .toFuture()
                    .get()
                    .getBody();
            return response.getResults().stream().filter(x-> x.getKnown_for_department()!= null && x.getKnown_for_department().equals("Acting")).toList();
        } catch (InterruptedException e) {
            return new ArrayList<>();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CastImage> extractCastImage(int level, String name){
        List<CastImage> result = new ArrayList<>();
        List<THMDBPerson> persons = findCast(name);
        if(persons.isEmpty())
            return new ArrayList<>();
        THMDBPersonImageResponse response = new THMDBPersonImageResponse();
        THMDBPerson person = null;
        try {
            if(level != 1){
                for (THMDBPerson p : persons){
                    if(p.isAdult()) {
                        person = p;
                        break;
                    }
                }
            }
            else{
                person = persons.get(0);
            }
            response = webClient.get()
                    .uri( "/person/" + person.getId() + "/images?api_key=" + "15d2ea6d0dc1d476efbca3eba2b9bbfb")
                    .retrieve()
                    .toEntity(THMDBPersonImageResponse.class)
                    .toFuture()
                    .get().getBody();
            for (THMDBPersonImage thmdbImage : response.getProfiles()) {
                CastImage mi = new CastImage();
                mi.setName(person.getName());
                mi.setAdult(person.isAdult() ? 1 : 0);
                mi.setCastGender(person.getGender());
                mi.setType("poster");
                mi.setSource("thmoviedb");
                mi.setUrl(thmdbImage.getFile_path());
                result.add(mi);
            }
        } catch (InterruptedException e) {
            return result;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


}
