package com.spark.movie.dto;

import com.spark.movie.model.Movie;
import com.spark.movie.repository.MovieRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    private ModelMapper mapper;
    private MovieRepository movieRepository;
    @Autowired
    MovieMapper(MovieRepository movieRepository){

        mapper = new ModelMapper();
        this.movieRepository = movieRepository;
        mapper.addMappings(new PropertyMap<Movie, MovieDTO>() {
            @Override
            protected void configure() {
                map().setParentId(source.getParent() != null ? source.getParent().getId() : null);
            }
        });
        configureMappings();
    }

    private void configureMappings() {

    }

    public MovieDTO createMovieDTO(Movie movie) {
        return this.mapper.map(movie, MovieDTO.class);
    }

    public Movie map(MovieDTO source, Movie destination) {
        this.mapper.map(source, destination);
        return destination;
    }
}
