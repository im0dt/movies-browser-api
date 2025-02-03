package com.spark.movie.service;

import com.spark.movie.model.Movie;
import com.spark.movie.model.MovieImage;
import com.spark.movie.repository.CategoryRepository;
import com.spark.movie.repository.MovieImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieImageService {

    private final MovieImageRepository movieImageRepository;

    @Autowired
    public MovieImageService(MovieImageRepository movieImageRepository) {
        this.movieImageRepository = movieImageRepository;
    }

    public Optional<MovieImage> getMovieImageById(int id) {
        return movieImageRepository.findById(id);
    }

    public void deleteMovieImage(int id){
        movieImageRepository.deleteById(id);
    }

    public void updateMovieImageType(int id, String type){
        MovieImage movieImage = movieImageRepository.findById(id).get();
        movieImage.setType(type);
        movieImageRepository.saveAndFlush(movieImage);
    }
}
