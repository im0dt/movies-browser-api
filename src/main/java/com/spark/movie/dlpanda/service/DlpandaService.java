package com.spark.movie.dlpanda.service;

import com.spark.movie.dto.ImageDTO;
import com.spark.movie.model.MovieImage;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DlpandaService {

    public List<ImageDTO> extractXImages(String url, String name){
        List<ImageDTO> movieImages = new ArrayList<>();
        System.out.println("Movie " + name + " - " + url);
        String res = getImagesUrl(url);
        if (!res.contains("https://pbs.twimg.com/media/"))
            return movieImages;
        String[] images = res.split("\n");
        movieImages.addAll(Arrays.stream(images).filter(x->x.startsWith("https://pbs.twimg.com/media/"))
                .map(x -> new ImageDTO(x, "backdrop", "X")).toList());
        return movieImages;
    }
    private String getImagesUrl(String url) {
        int exitCode=0;
        try {
            String[] CMD_ARRAY = { "python", "D:/Grabber/images.py", url};
            ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null){
                buffer.append(line + "\n");
            }
            exitCode = process.waitFor();
            return buffer.toString();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return "-1";
    }
}
