package com.spark.movie.service;

import com.spark.movie.model.Movie;
import com.spark.movie.model.MovieImage;
import com.spark.movie.pics.PicsImage;
import com.spark.movie.pics.PicsProcess;
import com.spark.movie.pics.PicsProperties;
import com.spark.movie.pics.PicsRequest;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PicsService {
    public static Map<String, Function<String, Predicate<PicsImage>>> predicateMap = new HashMap<>();
    static {
        predicateMap.put("IMDB_BACKDROP1", imdbId -> x -> x.getName() != null && x.getWidth() > 1000);
        predicateMap.put("IMDB_BACKDROP2", imdbId -> x -> x.getName() != null && x.getWidth() > 400);
        predicateMap.put("KINK_BACKDROP1", imdbId -> x -> x.getUrl().contains(imdbId) && (x.getWidth() == null || (x.getWidth() > x.getHeight() && x.getWidth() > 1000)));
        predicateMap.put("KINK_BACKDROP2", imdbId -> x -> x.getName() != null && (x.getWidth() == null || (x.getWidth() > x.getHeight() && x.getWidth() > 400)));
        predicateMap.put("KINK_POSTER", imdbId -> x -> x.getName() != null && (x.getWidth() == null || (x.getHeight() > x.getWidth())));
        predicateMap.put("DEEPER_POSTER1", imdbId -> x -> x.getUrl().contains(imdbId) && x.getWidth()!=null && x.getHeight() > x.getWidth() && x.getHeight() > 500);
        predicateMap.put("BRAZZER_POSTER1", imdbId -> x -> x.getUrl().contains(imdbId) && x.getUrl().contains("webp"));
        predicateMap.put("ADULTTIME_BACKDROP1", imdbId -> x -> x.getUrl().contains(imdbId) && (x.getWidth() == null || x.getWidth() > x.getHeight()));
        predicateMap.put("ADULTTIME_POSTER1", imdbId -> x -> x.getUrl().contains(imdbId)  && (x.getWidth() == null || x.getHeight() > x.getWidth()));
        predicateMap.put("PLAYBOYPLUSE_BACKDROP1", imdbId -> x -> x.getUrl().contains(imdbId) && (x.getWidth() == null || x.getWidth() > x.getHeight()));
        predicateMap.put("PLAYBOYPLUSE_POSTER1", imdbId -> x -> x.getUrl().contains(imdbId)  && (x.getWidth() == null || x.getHeight() > x.getWidth()));
        predicateMap = Collections.unmodifiableMap(predicateMap);
    }
    @Autowired
    private PicsProperties picsProperties;

    private final WebClient webClient;

    public PicsService(PicsProperties picsProperties){
        this.picsProperties = picsProperties;
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000 * 30);
            this.webClient = WebClient.builder().baseUrl("https://api.extract.pics")
                    //.clientConnector(new ReactorClientHttpConnector(httpClient))
                    //this.picsProperties.getApiUrl())
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE,
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .build();
    }

    private PicsProcess createPicsProcess(PicsRequest picsRequest){
        ResponseEntity<PicsProcess> response= null;
        try {
            response = webClient.post()
                    .uri("/v0/extractions")
                    .headers(h -> h.setBearerAuth(this.picsProperties.getApiKey()))
                    .bodyValue(picsRequest)
                    .retrieve()
                    .toEntity(PicsProcess.class)
                    .toFuture()
                    .get();
            return response.getBody();
            //                .subscribe(
//                        responseEntity -> {
//                            // Handle success response here
//
//                            String s = responseEntity.toString();
//                            // handle response as necessary
//                        },
//                        error -> {
//                            // Handle the error here
//                            if (error instanceof WebClientResponseException) {
//                                WebClientResponseException ex = (WebClientResponseException) error;
//                                HttpStatusCode status = ex.getStatusCode();
//                                System.out.println("Error Status Code: " + status.value());
//                                //...
//                            } else {
//                                // Handle other types of errors
//                                System.err.println("An unexpected error occurred: " + error.getMessage());
//                            }
//                        }
//                );
        } catch (InterruptedException e) {
            return new PicsProcess("error");
        } catch (ExecutionException e) {
            return new PicsProcess("error");
        }
    }

    private PicsProcess extratPicsProcess(String processId) {
        ResponseEntity<PicsProcess> response = null;
        try {
            response = webClient.get()
                    .uri("/v0/extractions/" + processId)
                    .headers(h -> h.setBearerAuth(this.picsProperties.getApiKey()))
                    .retrieve()
                    .toEntity(PicsProcess.class)
                    .toFuture()
                    .get();
            return response.getBody();
        } catch (InterruptedException | ExecutionException e) {
            return new PicsProcess("stop");
        }
    }

    private PicsImage[] extractMovieImages(PicsRequest picsRequest) {
        PicsProcess picsProcess = this.createPicsProcess(picsRequest);
        PicsProcess pp = null;
        while (!picsProcess.getData().getStatus().equals("done") &&
                !picsProcess.getData().getStatus().equals("error")) {
            pp = this.extratPicsProcess(picsProcess.getData().getId());
            try {
                if(pp.getData().getStatus().equals("done") ||
                        pp.getData().getStatus().equals("error")) {
                    System.out.println(picsRequest.getUrl() + ":" + pp.getData().getStatus());
                    break;
                }
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new PicsImage[0];
            }
        }
        return pp.getData().getImages();
    }

    interface ImageSelector {
        public boolean op(PicsImage a);
    }

    public List<MovieImage> extractMovieImage(String url, Predicate<PicsImage> backdropPredicate1, Predicate<PicsImage> backdropPredicate2, int backdropTolerance,
                                         Predicate<PicsImage> posterPredicate1, Predicate<PicsImage> posterPredicate2,int posterTolerance,
                                         String source) {
        PicsRequest pp = new PicsRequest(url);
        PicsImage[] retVal = extractMovieImages(pp);
        List<MovieImage> movieImages = new ArrayList<>();
        //backdrop
        if (backdropPredicate1 != null)
            movieImages.addAll(Arrays.stream(retVal).filter(backdropPredicate1)
                    .map(x -> new MovieImage(x.getUrl(), "backdrop", source, x.getWidth(), x.getHeight())).toList());
        if (backdropPredicate2 != null &&
                backdropPredicate1 != null &&
                Arrays.stream(retVal).filter(backdropPredicate1).count() <= backdropTolerance)
            movieImages.addAll(Arrays.stream(retVal).filter(backdropPredicate2)
                    .map(x -> new MovieImage(x.getUrl(), "backdrop", source, x.getWidth(), x.getHeight())).toList());

        //poster
        if (posterPredicate1 != null)
            movieImages.addAll(Arrays.stream(retVal).filter(posterPredicate1)
                    .map(x -> new MovieImage(x.getUrl(), "poster", source, x.getWidth(), x.getHeight())).toList());
        if (posterPredicate2 != null &&
                posterPredicate1 !=null &&
                Arrays.stream(retVal).filter(posterPredicate1).count() <= posterTolerance)
            movieImages.addAll(Arrays.stream(retVal).filter(posterPredicate2)
                    .map(x -> new MovieImage(x.getUrl(), "poster", source, x.getWidth(), x.getHeight())).toList());


        Map<String, List<MovieImage>> result
                = movieImages.stream().collect(
                Collectors.groupingBy(MovieImage::getUrl));
        Map<String, MovieImage> finalImages = new HashMap<>();
        for (var entry : result.entrySet()) {
            for (var img : entry.getValue()) {
                MovieImage currentImage = finalImages.get(entry.getKey());
                if (currentImage == null) {
                    finalImages.put(entry.getKey(), img);
                    currentImage = img;
                }
                if (currentImage.getWidth() ==null || (currentImage.getWidth()!=null && currentImage.getWidth() < img.getWidth()))
                    finalImages.put(entry.getKey(), img);
            }
        }
        return finalImages.values().stream().toList();
    }

    public PicsImage[] extractImdbImages(String imdbId) {
        PicsRequest pp = new PicsRequest(String.format(picsProperties.getImdbUrl(), imdbId));
        PicsImage[] retVal = extractMovieImages(pp);
        PicsImage[] images = Arrays.stream(retVal).filter(x -> x.getName() != null && !x.getName().contains("favicon") &&
                x.getSize() != null && !x.getName().contains(",") &&
                x.getBasename().contains("jpg") &&
                x.getWidth() > x.getHeight()).toArray(PicsImage[]::new);
        Map<String, List<PicsImage>> result
                = Arrays.stream(images).collect(
                Collectors.groupingBy(PicsImage::getbaseImegeCode));
        Map<String, PicsImage> finalImages = new HashMap<>();
        for (var entry : result.entrySet()) {
            for( var img : entry.getValue()){
                PicsImage currentImage = finalImages.get(entry.getKey());
                if( currentImage == null) {
                    finalImages.put(entry.getKey(), img);
                    currentImage = img;
                }
                if(currentImage.getWidth() < img.getWidth())
                    finalImages.put(entry.getKey(), img);
            }
        }
        return finalImages.values().toArray(new PicsImage[0]);
    }

    public PicsImage[] extractAZImages(String movieUrl) {
        PicsRequest pp = new PicsRequest(movieUrl);
        PicsImage[] retVal = extractMovieImages(pp);
        PicsImage[] images = Arrays.stream(retVal).filter(x -> x.getName() != null &&
                x.getWidth() > 1000
                ).toArray(PicsImage[]::new);
        if(images.length == 0){
            images = Arrays.stream(retVal).filter(x -> x.getName() != null &&
                    x.getWidth() > 400
            ).toArray(PicsImage[]::new);
        }
        Map<String, List<PicsImage>> result
                = Arrays.stream(images).collect(
                Collectors.groupingBy(PicsImage::getbaseImegeCode));
        Map<String, PicsImage> finalImages = new HashMap<>();
        for (var entry : result.entrySet()) {
            for( var img : entry.getValue()){
                PicsImage currentImage = finalImages.get(entry.getKey());
                if( currentImage == null) {
                    finalImages.put(entry.getKey(), img);
                    currentImage = img;
                }
                if(currentImage.getWidth() < img.getWidth())
                    finalImages.put(entry.getKey(), img);
            }
        }
        return finalImages.values().toArray(new PicsImage[0]);
    }

    public PicsImage[] extractKImages(String imdbId,String movieUrl) {
        PicsRequest pp = new PicsRequest(movieUrl);
        PicsImage[] retVal = extractMovieImages(pp);
        PicsImage[] images = Arrays.stream(retVal).filter(x -> x.getUrl().contains(imdbId) &&
                x.getWidth() !=null &&  x.getWidth() > 1000
        ).toArray(PicsImage[]::new);
        if(images.length < 5){
            images = Arrays.stream(retVal).filter(x -> x.getUrl().contains(imdbId) &&
                    x.getWidth() !=null &&  x.getWidth() >= 400
            ).toArray(PicsImage[]::new);
        }
        Map<String, List<PicsImage>> result
                = Arrays.stream(images).collect(
                Collectors.groupingBy(PicsImage::getbaseImegeCode));
        Map<String, PicsImage> finalImages = new HashMap<>();
        for (var entry : result.entrySet()) {
            for( var img : entry.getValue()){
                PicsImage currentImage = finalImages.get(entry.getKey());
                if( currentImage == null) {
                    finalImages.put(entry.getKey(), img);
                    currentImage = img;
                }
                if(currentImage.getWidth() < img.getWidth())
                    finalImages.put(entry.getKey(), img);
            }
        }
        return finalImages.values().toArray(new PicsImage[0]);
    }

}
