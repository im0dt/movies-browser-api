package com.spark.movie.controller;

import com.spark.movie.dto.ImageDTO;
import com.spark.movie.service.CastService;
import com.spark.movie.service.MovieService;
import com.spark.movie.vk.model.VKRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/vk")
public class VKController {
    @Autowired
    MovieService movieService ;
    @Autowired
    CastService castService ;

    @PostMapping("/{opId}")
    public List<ImageDTO> get(@PathVariable String opId, @RequestBody VKRequest request) {
        if(request.getSource().equals("M")){
            return movieService.extractVKMovieImage(opId, Integer.parseInt(request.getSourceId()), request.getId(),
                    request.getOwnerId(), request.getApiKey(), request.getOffset(), request.getCount());
        }
        if(request.getSource().equals("C")){
            return castService.extractVKCastImage(opId, request.getSourceId(), request.getId(), request.getOwnerId(),
                    request.getApiKey(), request.getOffset(), request.getCount());
        }
        return new ArrayList<>();
    }


}
