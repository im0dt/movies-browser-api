package com.spark.movie.tmdb.model;

import java.util.List;

public class THMDBPersonImageResponse {
    private List<THMDBPersonImage> profiles;

    public List<THMDBPersonImage> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<THMDBPersonImage> profiles) {
        this.profiles = profiles;
    }
}
