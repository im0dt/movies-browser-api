package com.spark.movie.model;

import com.spark.movie.dto.MovieDTO;

import java.util.ArrayList;
import java.util.List;

public class Cast {
    private String id;
    private String name;

    List<MovieDTO> movies;

    List<CastImage> images = new ArrayList<>();

    public Cast(String name, List<MovieDTO> movies) {
        this.id = name;
        this.name = name;
        this.movies = movies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CastImage> getImages() {
        return images;
    }

    public void setImages(List<CastImage> images) {
        this.images = images;
    }

    public List<MovieDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieDTO> movies) {
        this.movies = movies;
    }
}
