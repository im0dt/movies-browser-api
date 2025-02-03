package com.spark.movie.controller;

import com.spark.movie.dlpanda.model.XUrl;
import com.spark.movie.dlpanda.model.XUrlRequest;
import com.spark.movie.dto.ImageDTO;
import com.spark.movie.model.Movie;
import com.spark.movie.model.MovieImage;
import com.spark.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/dlPanda")
public class DlpandaController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public List<ImageDTO> extractdlpandaMovieImage(@RequestBody XUrlRequest request) {
        List<ImageDTO> result = new ArrayList<>();
        for(String url : request.getxUrls())
            result.addAll(movieService.extractdlpandaMovieImage(url, request.getId()));
        return  result;
    }
}
