package com.spark.movie.controller;

import com.spark.movie.model.Movie;
import com.spark.movie.service.VideoDownloaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/videoDownloader")
public class VideoDownloaderController {
    @Autowired
    VideoDownloaderService videoDownloaderService;

    @GetMapping
    public ResponseEntity<Void> downloadAllMoviesVideos(@RequestParam("l") int level, @RequestParam("type") String type) {
        videoDownloaderService.downloadVideos(level, type);
        return ResponseEntity.noContent().build();
    }
}
