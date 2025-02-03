package com.spark.movie.ppics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spark.movie.dto.ImageDTO;
import com.spark.movie.ppics.model.PpicsGalleryResponse;
import com.spark.movie.ppics.model.PpicsPosts;
import com.spark.movie.ppics.model.PpicsResult;
import com.spark.movie.ppics.model.PpicsSearchReponse;
import com.spark.movie.vk.model.VKAttachment;
import com.spark.movie.vk.model.VKData;
import com.spark.movie.vk.model.VKItem;
import com.spark.movie.vk.model.VKSize;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PpicsService {
    private final WebClient webClient;

    public PpicsService() {
        this.webClient = WebClient.builder().baseUrl("https://api.adultdatalink.com/pornpics")//this.picsProperties.getApiUrl())
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE,
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private Mono<InputStream> apply(Flux<DataBuffer> dataBufferFlux) {
        return DataBufferUtils
                .join(dataBufferFlux)
                .publishOn(Schedulers.boundedElastic())
                .map(dataBuffer -> dataBuffer.asInputStream(true)); // Automatically releases the buffer on read
    }

    public List<ImageDTO> extractImages(String name) {
        List<ImageDTO> result = new ArrayList<>();
        PpicsSearchReponse searchReponse = new PpicsSearchReponse();
        Flux<DataBuffer> dataBufferFlux;
        dataBufferFlux = webClient.get()
                .uri("/search?query=" + name)

                .retrieve()
                .bodyToFlux(DataBuffer.class);
        Mono<InputStream> resp = this.apply(dataBufferFlux);
        ObjectMapper mapper = new ObjectMapper();
        try {
            searchReponse = mapper.readValue(resp.block(), PpicsSearchReponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(searchReponse.detail !=null){
            return result;
        }
        for(PpicsPosts post : searchReponse.results.posts) {
            PpicsGalleryResponse galleryResponse = new PpicsGalleryResponse();
            if(searchReponse.detail !=null){
                return result;
            }
            try {
                URI uri = new URI(post.gallery_url);
                String path = uri.getPath();
                String[] segments = path.split("/");
                String postTitle = segments[segments.length - 1];
                dataBufferFlux = webClient.get()
                        .uri("/gallery-image-links?gallery=" + postTitle)
                        .retrieve()
                        .bodyToFlux(DataBuffer.class);
                resp = this.apply(dataBufferFlux);

                galleryResponse = mapper.readValue(resp.block(), PpicsGalleryResponse.class);
            } catch (URISyntaxException | IOException | WebClientResponseException | ArrayIndexOutOfBoundsException ex ) {
                continue;
            }
            if(galleryResponse.urls != null){
                for(String url : galleryResponse.urls.link_list){
                    result.add(new ImageDTO(url,
                            "poster", "PICS"));
                }
            }
        }
        return result;
    }
}
