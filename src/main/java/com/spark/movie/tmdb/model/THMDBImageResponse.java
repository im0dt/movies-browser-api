package com.spark.movie.tmdb.model;

import java.util.List;

public class THMDBImageResponse {
    private List<THMDBPersonImage> posters;
    private List<THMDBPersonImage> backdrops;

    public List<THMDBPersonImage> getPosters() {
        return posters;
    }

    public void setPosters(List<THMDBPersonImage> posters) {
        this.posters = posters;
    }

    public List<THMDBPersonImage> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<THMDBPersonImage> backdrops) {
        this.backdrops = backdrops;
    }
}
