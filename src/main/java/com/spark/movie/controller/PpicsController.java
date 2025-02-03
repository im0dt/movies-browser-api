package com.spark.movie.controller;

import com.spark.movie.dto.ImageDTO;
import com.spark.movie.ppics.model.PpicsRequest;
import com.spark.movie.service.CastService;
import com.spark.movie.service.MovieService;
import com.spark.movie.vk.model.VKRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/ppics")
public class PpicsController {
    @Autowired
    MovieService movieService;
    @Autowired
    CastService castService;

    @PostMapping
    public List<ImageDTO> get(@RequestBody PpicsRequest request) {
        if (request.getSource().equals("M")) {

        }
        if (request.getSource().equals("C")) {
            return castService.extractPpicsCastImage(request.getName());
        }
        return new ArrayList<>();
    }
}
