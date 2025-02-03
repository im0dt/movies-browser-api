package com.spark.movie.controller;

import com.spark.movie.model.Cast;
import com.spark.movie.model.CastImage;
import com.spark.movie.service.CastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/castsupdates")
public class CastUpdateController {

    @Autowired
    private CastService castService;

    @GetMapping
    public ResponseEntity<Void> updateAllCasts(@RequestParam("l") Integer level) {
        List<Cast> casts = castService.listAllCastes(level);
        for(Cast cast : casts) {
            castService.extractCastImages(level, cast.getName());
        }
        return ResponseEntity.ok().build();
    }
}
