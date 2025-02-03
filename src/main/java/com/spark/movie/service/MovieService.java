package com.spark.movie.service;

import com.spark.movie.dlpanda.service.DlpandaService;
import com.spark.movie.dto.ImageDTO;
import com.spark.movie.dto.MovieImageMapper;
import com.spark.movie.model.*;
import com.spark.movie.repository.MovieImageRepository;
import com.spark.movie.repository.MovieRepository;
import com.spark.movie.repository.YearRangeRepository;
import com.spark.movie.tmdb.service.THMDBService;
import com.spark.movie.vk.service.VKService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final YearRangeRepository yearRangeRepository;
    private final MovieImageRepository movieImageRepository;
    @Autowired
    private final THMDBService thmdbService;
    @Autowired
    private final PicsService picsService;
    @Autowired
    private final VKService vkService;
    @Autowired
    private final DlpandaService dlpandaService;
    @Autowired
    private final MovieImageMapper movieImageMapper;

    @Autowired
    public MovieService(MovieRepository movieRepository,
                        MovieImageRepository movieImageRepository,
                        YearRangeRepository yearRangeRepository,
                        THMDBService thmdbService,
                        PicsService picsService,
                        VKService vkService,
                        MovieImageMapper movieImageMapper,
                        DlpandaService dlpandaService) {
        this.movieRepository = movieRepository;
        this.movieImageRepository = movieImageRepository;
        this.yearRangeRepository = yearRangeRepository;
        this.thmdbService = thmdbService;
        this.picsService = picsService;
        this.vkService = vkService;
        this.movieImageMapper = movieImageMapper;
        this.dlpandaService = dlpandaService;
    }

    public List<Movie> getAllMovies(int level) {
        List<Movie> movies = movieRepository.findAll(level);
        for (Movie movie : movies){
            movie.setPosters(getMovieImages(movie.getId(), "poster").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setBackdrops(getMovieImages(movie.getId(), "backdrop").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setHeros(getMovieImages(movie.getId(), "hero").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setVideos(getMovieImages(movie.getId(), "movie").stream().map(x -> new Video(x.getSource(), x.getUrl(), x.getLocalPath())).collect(Collectors.toList()));
        }
        return movies;
    }

    public List<Movie> getAllEpisodes(Integer parentId) {
        List<Movie> movies = movieRepository.findByParentId(parentId);
        for (Movie movie : movies){
            movie.setPosters(getMovieImages(movie.getId(), "poster").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setBackdrops(getMovieImages(movie.getId(), "backdrop").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setHeros(getMovieImages(movie.getId(), "hero").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setVideos(getMovieImages(movie.getId(), "movie").stream().map(x -> new Video(x.getSource(), x.getUrl(), x.getLocalPath())).collect(Collectors.toList()));
        }
        return movies;
    }

    public Movie getMovieById(int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isEmpty())
            return movie.get();
        movie.get().setPosters(getMovieImages(id, "poster").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
        movie.get().setBackdrops(getMovieImages(id, "backdrop").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
        movie.get().setHeros(getMovieImages(id, "hero").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
        movie.get().setVideos(getMovieImages(id, "movie").stream().map(x -> new Video(x.getSource(), x.getUrl(), x.getLocalPath())).collect(Collectors.toList()));
        return movie.get();
    }

    public Movie createMovie(Movie movie, Integer parentId) {
        if(parentId!=null)
            movie.setParent(movieRepository.getById(parentId));
        return movieRepository.save(movie);
    }

    public void updateMovie(Movie movie, Integer parentId){
        if(parentId!=null)
            movie.setParent(movieRepository.getById(parentId));
        movieRepository.save(movie);
    }

    public void createMovieImages(List<MovieImage> movieImages) {
        movieImageRepository.saveAllAndFlush(movieImages);
    }

    public MovieImage createMovieImage(MovieImage movieImage) {
        return movieImageRepository.saveAndFlush(movieImage);
    }
    public void deleteMovie(int id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }
    public List<Movie> getAllMoviesByCategoryId(int categoryId, int count) {
        List<Movie> movies = movieRepository.findByCategory(categoryId, PageRequest.of(0,count));
        for (Movie movie : movies){
            movie.setPosters(getMovieImages(movie.getId(), "poster").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setHeros(getMovieImages(movie.getId(), "hero").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setBackdrops(getMovieImages(movie.getId(), "backdrop").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
        }
        return movies;
    }

    public List<Movie> getAllMoviesByRangeId(int rangeId, Integer level, int count) {
        YearRange range = yearRangeRepository.findById(rangeId).get();
        List<Movie> movies = movieRepository.findByRange(range.getFrom(), range.getTo(), level,PageRequest.of(0,count));
        for (Movie movie : movies){
            movie.setPosters(getMovieImages(movie.getId(), "poster").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath()))   .collect(Collectors.toList()));
            movie.setHeros(getMovieImages(movie.getId(), "hero").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setBackdrops(getMovieImages(movie.getId(), "backdrop").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
        }
        return movies;
    }

    public List<Movie> getAllMoviesByLanguage(String language, Integer level, int count) {
        List<Movie> movies = movieRepository.findByLanguage(language, level, PageRequest.of(0,count));
        for (Movie movie : movies){
            movie.setPosters(getMovieImages(movie.getId(), "poster").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setHeros(getMovieImages(movie.getId(), "hero").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
            movie.setBackdrops(getMovieImages(movie.getId(), "backdrop").stream().map(x -> new Image(x.getId(), x.getSource(), x.getUrl(), x.getType(), x.getLocalPath())).collect(Collectors.toList()));
        }
        return movies;
    }

    public Movie getMovieByImdbId(String imdbId) {
        return movieRepository.findByImdbId(imdbId);
    }
    public List<MovieImage> getMovieImages(int movieId, String type) {
        return movieImageRepository.listMovieImages(movieId, type);
    }

    @Transactional
    public List<MovieImage> extractMovieImages(int id) {
        List<MovieImage> result = new ArrayList<>();
        Movie movie = movieRepository.findById(id).get();
        if (movie.getSource().equals("IMDB")) {
            for (MovieImage img : thmdbService.extractMovieImage(movie.getImdbid())) {
                if (movieImageRepository.findByUrl(movie.getId(), img.getType(), img.getUrl()) == null) {
                    img.setMovie(movie);
                    result.add(img);
                }
            }
            for (MovieImage img : thmdbService.extractMovieVideo(movie.getImdbid())) {
                if (movieImageRepository.findByUrl(movie.getId(), img.getType(), img.getUrl()) == null) {
                    img.setMovie(movie);
                    result.add(img);
                }
            }
        }
        if (movie.getUrl() != null) {
            List<MovieImage> picsImage = picsService.extractMovieImage(
                    movie.getUrl(),
                    PicsService.predicateMap.get(movie.getSource() + "_BACKDROP1") !=null ?
                            PicsService.predicateMap.get(movie.getSource() + "_BACKDROP1").apply(movie.getImdbid()) : null,
                    PicsService.predicateMap.get(movie.getSource() + "_BACKDROP2") !=null ?
                            PicsService.predicateMap.get(movie.getSource() + "_BACKDROP2").apply(movie.getImdbid()): null,
                    5,
                    PicsService.predicateMap.get(movie.getSource() + "_POSTER1") != null ?
                            PicsService.predicateMap.get(movie.getSource() + "_POSTER1").apply(movie.getImdbid()) : null,
                    PicsService.predicateMap.get(movie.getSource() + "_POSTER2")!=null ?
                            PicsService.predicateMap.get(movie.getSource() + "_POSTER2").apply(movie.getImdbid()) : null,
                    0,
                    movie.getSource());
            for (MovieImage img : picsImage) {
                if (movieImageRepository.findByUrl(movie.getId(), img.getType(), img.getUrl()) == null) {
                    img.setMovie(movie);
                    result.add(img);
                }
            }
            if (movie.getSource().equals("IMDB")) {
                List<MovieImage> movieImages = new ArrayList<>();
                System.out.println("Movie " + movie.getName() + " - " + movie.getUrl());
                String res = getAZVideosUrl(movie.getUrl());
                if (res.contains("aznude.com"))
                {
                    String[] images = res.split("\n");
                    movieImages.addAll(Arrays.stream(images)
                            .map(x -> new MovieImage(x, "movie", "AZ", null, null)).toList());
                    for (MovieImage img : movieImages) {
                        if (movieImageRepository.findByUrl(movie.getId(), img.getType(), img.getUrl()) == null) {
                            img.setMovie(movie);
                            result.add(img);
                        }
                    }
                }
            }
        }
        createMovieImages(result);
        return result;
    }

    private String getAZVideosUrl(String url) {
        int exitCode=0;
        try {
            String[] CMD_ARRAY = { "python", "D:/Grabber/videos.py", url};
            ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null){
                buffer.append(line + "\n");
            }
            exitCode = process.waitFor();
            return buffer.toString();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return "-1";
    }

    public List<ImageDTO> extractVKMovieImage(String opId, int movieId, String id, String ownerId, String apiKey, int offset, int count) {
        List<MovieImage> result = new ArrayList<>();
        Movie movie = movieRepository.findById(movieId).get();
        List<ImageDTO> res = new ArrayList<>();
        res = vkService.extractImages(apiKey, opId, ownerId, id, offset, count);
        for (ImageDTO img : res) {
            if (movieImageRepository.findByUrl(movie.getId(), img.getType(), img.getUrl()) == null) {
                MovieImage movieImage = new MovieImage();
                movieImageMapper.mapToMovieImage(img, movieImage);
                movieImage.setMovie(movie);
                result.add(movieImage);
            }
        }
        createMovieImages(result);
        return res;
    }

    public List<ImageDTO> extractdlpandaMovieImage(String url, int movieId) {
        List<MovieImage> result = new ArrayList<>();
        Movie movie = movieRepository.findById(movieId).get();
        List<ImageDTO> res = new ArrayList<>();
        res = dlpandaService.extractXImages(url, movie.getName());
        for (ImageDTO img : res) {
            if (movieImageRepository.findByUrl(movie.getId(), img.getType(), img.getUrl()) == null) {
                MovieImage movieImage = new MovieImage();
                movieImageMapper.mapToMovieImage(img, movieImage);
                movieImage.setMovie(movie);
                result.add(movieImage);
            }
        }
        createMovieImages(result);
        return res;
    }

    public void setImageProcessed(int imageId, String status, String localPath){
        MovieImage movieImage = movieImageRepository.getById(imageId);
        movieImage.setProcessed(status);
        movieImage.setLocalPath(localPath);
        movieImageRepository.save(movieImage);

    }
}

