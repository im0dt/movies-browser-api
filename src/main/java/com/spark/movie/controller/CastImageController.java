package com.spark.movie.controller;

import com.spark.movie.dto.ImageDTO;
import com.spark.movie.model.CastImage;
import com.spark.movie.service.CastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/castimages")
public class CastImageController {

    @Autowired
    private CastService castService;
    @GetMapping
    public List<CastImage> extractCastImage(@RequestParam("l") Integer level, @RequestParam("name") String name) {
        return castService.extractCastImages(level, name);
    }

    @PostMapping
    public ResponseEntity<Void> createCastImage(@RequestBody ImageDTO ImageDTO) {
        castService.createCastImage(ImageDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCastImage(@PathVariable Integer id, @RequestParam("type") String type) {
        castService.updateCastImageType(id, type);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieImage(@PathVariable Integer id) {
        castService.deleteCastImage(id);
        return ResponseEntity.ok().build();
    }
}
