package com.spark.movie.dto;

import com.spark.movie.model.CastImage;
import com.spark.movie.model.MovieImage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MovieImageMapper {

    private final ModelMapper mapper;
    MovieImageMapper(){

        mapper = new ModelMapper();
    }

    public ImageDTO createMovieImageDTO(MovieImage movieImage) {
        return this.mapper.map(movieImage, ImageDTO.class);
    }

    public void mapToMovieImage(ImageDTO source, MovieImage destination) {
        this.mapper.map(source, destination);
    }

    public CastImage mapToCastImage(ImageDTO source, CastImage destination) {
        this.mapper.map(source, destination);
        return destination;
    }
}
