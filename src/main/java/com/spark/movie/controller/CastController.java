package com.spark.movie.controller;

import com.spark.movie.dto.MovieDTO;
import com.spark.movie.model.*;
import com.spark.movie.service.CastService;
import com.spark.movie.vk.model.VKRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/casts")
public class CastController {
    @Autowired
    private CastService castService;


    @GetMapping("/{opId}")
    public List<Cast> getAllCategories(@PathVariable String opId,
                                                 @RequestParam(value = "l", required = false) Integer level,
                                                 @RequestParam(value = "name", required = false) String name) {
        if(opId.equals("all"))
            return castService.listAllCastes(level);
        if(opId.equals("single"))
            return Arrays.asList(castService.getCastByName(name)) ;
        return new ArrayList<>();
    }







}
