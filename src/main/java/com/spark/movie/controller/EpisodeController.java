package com.spark.movie.controller;

import com.spark.movie.model.Movie;
import com.spark.movie.repository.MovieRepository;
import com.spark.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/episodes")
public class EpisodeController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public List<Movie> getAllMovies(@PathVariable int id) {

        return movieService.getAllEpisodes(id);
    }
}
