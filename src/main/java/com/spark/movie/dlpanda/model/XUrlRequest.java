package com.spark.movie.dlpanda.model;

import java.util.List;

public class XUrlRequest {
    private int id;
    private List<String> xUrls;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getxUrls() {
        return xUrls;
    }

    public void setxUrls(List<String> xUrls) {
        this.xUrls = xUrls;
    }
}
