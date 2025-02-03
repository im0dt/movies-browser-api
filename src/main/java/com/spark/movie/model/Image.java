package com.spark.movie.model;

public class Image {
    public Image(int id, String source, String url, String type, String localPath) {
        this.id = id;
        this.source = source;
        this.url = url;
        this.type = type;
        this.localPath = localPath;
    }
    private int id;
    private String source;
    private String url;
    private String type;
    private String localPath;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
