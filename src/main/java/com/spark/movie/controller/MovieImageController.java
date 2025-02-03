package com.spark.movie.controller;

import com.spark.movie.model.Movie;
import com.spark.movie.model.MovieImage;
import com.spark.movie.service.MovieImageService;
import com.spark.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/movieImages")
public class MovieImageController
{
    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieImageService movieImageService;

    @GetMapping("/{id}")
    public List<MovieImage> getImageByMovieId(@PathVariable int id, @RequestParam("type") String type) {
        Collections.sort(movieService.getMovieImages(id, type),  Comparator.comparing(MovieImage::getSource));
        return movieService.getMovieImages(id, type);
    }

    @PostMapping("/{id}")
    public MovieImage createMovieImage(@PathVariable Integer id, @RequestBody MovieImage movieImage) {
        Movie myMovie = movieService.getMovieById(id);
        movieImage.setMovie(myMovie);
        return movieService.createMovieImage(movieImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> createMovieImage(@PathVariable Integer id, @RequestParam("type") String type) {
        movieImageService.updateMovieImageType(id, type);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieImage(@PathVariable Integer id) {
        movieImageService.deleteMovieImage(id);
        return ResponseEntity.ok().build();
    }
}
