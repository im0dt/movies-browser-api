package com.spark.movie.dto;

import com.spark.movie.model.StringListConverter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class MovieDTO {

    public MovieDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private int id;
    private String imdbid;
    private String name;
    private String aka;
    private int year;
    private int duration;
    private double rating;
    private int votes;
    private String country;
    private String languages;
    private String genres;
    private String plotoutline;
    private String url;
    private boolean seen;
    private Integer level;
    private String source;
    private Integer type;
    private Integer parentId;
    @Convert(converter = StringListConverter.class)
    private List<String> cast;
    private List<ImageDTO> backdrops;
    private List<ImageDTO> posters;
    private List<ImageDTO> heros;
    private List<ImageDTO> videos;
    private List<MovieDTO> episodes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdbid() {
        return imdbid;
    }

    public void setImdbid(String imdbid) {
        this.imdbid = imdbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAka() {
        return aka;
    }

    public void setAka(String aka) {
        this.aka = aka;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPlotoutline() {
        return plotoutline;
    }

    public void setPlotoutline(String plotoutline) {
        this.plotoutline = plotoutline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<ImageDTO> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<ImageDTO> backdrops) {
        this.backdrops = backdrops;
    }

    public List<ImageDTO> getPosters() {
        return posters;
    }

    public void setPosters(List<ImageDTO> posters) {
        this.posters = posters;
    }

    public List<ImageDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<ImageDTO> videos) {
        this.videos = videos;
    }

    public List<String>  getCast() {
        return cast;
    }

    public void setCast(List<String>  cast) {
        this.cast = cast;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<ImageDTO> getHeros() {
        return heros;
    }

    public void setHeros(List<ImageDTO> heros) {
        this.heros = heros;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<MovieDTO> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<MovieDTO> episodes) {
        this.episodes = episodes;
    }
}
