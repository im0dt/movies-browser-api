package com.spark.movie.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imdbid;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String aka;
    private int year;
    private int duration;
    private double rating;
    private int votes;
    @Column(columnDefinition = "TEXT")
    private String country;
    @Column(columnDefinition = "TEXT")
    private String languages;
    @Column(columnDefinition = "TEXT")
    private String genres;
    @Convert(converter = StringListConverter.class)
    @Column(name = "cast", nullable = false)
    private List<String> cast = new ArrayList<>();;
    @Column(columnDefinition = "TEXT")
    private String plotoutline;
    @Column(columnDefinition = "TEXT")
    private String url;
    private boolean seen;
    private String source;
    private Integer level;
    private Integer type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Movie parent;
    @Transient
    private List<Image> backdrops;
    @Transient
    private List<Image> posters;
    @Transient
    private List<Image> heros;
    @Transient
    private List<Video> videos;
    @Transient
    private List<Movie> episodes;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<Image> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<Image> backdrops) {
        this.backdrops = backdrops;
    }

    public List<Image> getPosters() {
        return posters;
    }

    public void setPosters(List<Image> posters) {
        this.posters = posters;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<String> getCast() {
        return cast;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public List<Image> getHeros() {
        return heros;
    }

    public void setHeros(List<Image> heros) {
        this.heros = heros;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Movie getParent() {
        return parent;
    }

    public void setParent(Movie parent) {
        this.parent = parent;
    }

    public List<Movie> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Movie> episodes) {
        this.episodes = episodes;
    }
}
