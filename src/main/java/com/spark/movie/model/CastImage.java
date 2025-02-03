package com.spark.movie.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cast_images")
public class CastImage {
    public CastImage(){

    }

    public CastImage(int id, String name, String url, String type, String source) {
        this.url = url;
        this.type = type;
        this.source = source;
        this.id = id;
    }

    public CastImage(String name, String url, String type, String source, Integer width, Integer height) {
        this.url = url;
        this.type = type;
        this.source = source;
    }

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String url;
    @Column(columnDefinition = "TEXT")
    private String type;
    @Column(columnDefinition = "TEXT", name ="imagesource")
    private String source;
    @Column(length = 1)
    private String processed;
    private String localPath;
    @Transient
    private Integer width;
    @Transient
    private Integer height;
    private Integer castGender;
    private Integer adult;
    private int deleted;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String movie) {
        this.name = movie;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getCastGender() {
        return castGender;
    }

    public void setCastGender(Integer castGender) {
        this.castGender = castGender;
    }

    public Integer getAdult() {
        return adult;
    }

    public void setAdult(Integer adult) {
        this.adult = adult;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
