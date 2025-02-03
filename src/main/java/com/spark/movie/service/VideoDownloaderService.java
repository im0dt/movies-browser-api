package com.spark.movie.service;

import com.spark.movie.model.Movie;
import com.spark.movie.model.MovieImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class VideoDownloaderService
{
    @Autowired
    private  MovieService movieService;

    public VideoDownloaderService(MovieService movieService){
        this.movieService = movieService;
    }

    public void downloadVideos(Integer level, String type) {
        List<Movie> movies = movieService.getAllMovies(level);
        for (Movie movie : movies) {
            List<MovieImage> videos = movieService.getMovieImages(movie.getId(), type);
            for (MovieImage vid : videos) {
                try {
                    if (vid.getProcessed() != null && (vid.getProcessed().equals("D") || vid.getProcessed().equals("F")))
                        continue;
                    if (vid.getSource().equals("X") && vid.getType().equals("movie")) {
                        System.out.println("Movie " + movie.getName() + " - " + vid.getUrl());
                        String res = getXVideoUrl(vid.getUrl(), movie.getImdbid(), "AZ" + vid.getId() + ".mp4");
                        //vid.setUrl(res);
                        //vid.setSource("AZ");
                        if (res.isEmpty() || !res.contains("status:done"))
                            continue;
                        movieService.setImageProcessed(vid.getId(), "D","/AZ" + vid.getId() + ".mp4");
                    }
                    if (!type.equals("movie") || (vid.getSource() != null && vid.getSource().equals("AZ"))) {
                        if (!Files.exists(Path.of("D:/Grabber/" + movie.getImdbid()))) {
                            Files.createDirectories(Path.of("D:/Grabber/" + movie.getImdbid()));
                        }
                        String url = vid.getSource().equals("thmoviedb") ? "https://image.tmdb.org/t/p/original" + vid.getUrl() : vid.getUrl();
                        BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                        String ext = type.equals("movie") ? ".mp4" : vid.getUrl().contains(".png") ? ".png" :
                                vid.getUrl().contains(".webp") ? ".webp" : ".jpg";
                        FileOutputStream fileOutputStream = new FileOutputStream("D:/Grabber/" + movie.getImdbid() + "/AZ" + vid.getId() + ext);
                        int completed = 0;
                        byte dataBuffer[] = new byte[819200];
                        int bytesRead;
                        while ((bytesRead = in.read(dataBuffer, 0, 819200)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                            completed = completed + 100;
                            System.out.println("Movie " + movie.getName() + " - AZ" + vid.getId() + ", " + (completed / 1000) + " KB");
                        }
                        fileOutputStream.close();
                        movieService.setImageProcessed(vid.getId(), "D","/AZ" + vid.getId() + ext);
                    }
                } catch (FileNotFoundException e) {
                    movieService.setImageProcessed(vid.getId(), "F", null);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String getXVideoUrl(String url, String imdbid, String id) {
        int exitCode=0;
        try {
            String[] CMD_ARRAY = { "python", "D:/Grabber/tes.py", url, imdbid, id};
            ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            exitCode = process.waitFor();
            return buffer.toString();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return "-1";
    }
}
