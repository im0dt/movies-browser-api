package com.spark.movie.controller;

import com.spark.movie.dto.MovieMapper;
import com.spark.movie.model.Movie;
import com.spark.movie.dto.MovieDTO;
import com.spark.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/movies")
class MoviesController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieMapper movieMapper;
    @GetMapping
    public List<Movie> getAllMovies(@RequestParam("l") Integer level, @RequestParam(value = "parent_id", required = false) Integer parentId) {
        if(parentId == null)
            return movieService.getAllMovies(level);
        else
            return movieService.getAllEpisodes(parentId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable int id) {
        return ResponseEntity.ok(movieMapper.createMovieDTO(movieService.getMovieById(id)));
    }

    @PostMapping
    ResponseEntity replaceEmployee(@RequestBody MovieDTO newMovie) {
        Movie movie = new Movie();
        movieMapper.map(newMovie, movie);
        movie.setCast(newMovie.getCast());
        movieService.updateMovie(movie, newMovie.getParentId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    ResponseEntity replaceEmployee(@RequestBody MovieDTO newMovie, @PathVariable int id) {
            Movie movie = movieService.getMovieById(id);
            movie = movieMapper.map(newMovie, movie);
            movie.setCast(newMovie.getCast());
            movieService.updateMovie(movie, newMovie.getParentId());
            return ResponseEntity.ok().build();
    }

//    @RequestMapping(value = "/updateImages", produces = "application/json",  method=RequestMethod.POST)
//    public Movie updateImages(@RequestBody Movie movie) {
//        Movie myMovie = movieRepository.findByImdbId(movie.getImdbid());
//        myMovie.setPoster(movie.getPoster());
//        myMovie.setBackdrop(movie.getBackdrop());
//        movieRepository.save(myMovie);;
//        return myMovie;
//    }

}