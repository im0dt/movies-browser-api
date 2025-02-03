package com.spark.movie.tmdb.model;

import java.util.List;

public class THMDBFindPersonResponse {
    private List<THMDBPerson> results;

    public List<THMDBPerson> getResults() {
        return results;
    }

    public void setResults(List<THMDBPerson> results) {
        this.results = results;
    }
}
