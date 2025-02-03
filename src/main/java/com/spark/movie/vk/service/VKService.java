package com.spark.movie.vk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spark.movie.dto.ImageDTO;
import com.spark.movie.vk.model.*;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class VKService {

    public static Map<String, String> queryStringMAp = new HashMap<>();

    static {
        queryStringMAp.put("photos.getAll", "owner_id=%s");
        queryStringMAp.put("wall.getById", "posts=%s");
        queryStringMAp.put("photos.get", "album_id=%s&owner_id=%s");
        queryStringMAp = Collections.unmodifiableMap(queryStringMAp);
    }

    private final WebClient webClient;

    public VKService() {
        this.webClient = WebClient.builder().baseUrl("https://api.vk.com/method")//this.picsProperties.getApiUrl())
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE,
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<InputStream> apply(Flux<DataBuffer> dataBufferFlux) {
        return DataBufferUtils
                .join(dataBufferFlux)
                .publishOn(Schedulers.boundedElastic())
                .map(dataBuffer -> dataBuffer.asInputStream(true)); // Automatically releases the buffer on read
    }

    public List<ImageDTO> extractImages(String apiKey, String opId, String id0, String id1, int offset, int count) {
        List<ImageDTO> result = new ArrayList<>();
        String queryString = String.format(queryStringMAp.get(opId), id0, id1);
        VKData data = new VKData();
        Flux<DataBuffer> dataBufferFlux;
        dataBufferFlux = webClient.get()
                .uri("/" + opId + "?v=5.199&access_token=" + apiKey + "&" + queryString + "&offset=" + offset + "&count=" + count)

                .retrieve()
                .bodyToFlux(DataBuffer.class);
//                    .toEntity(VKData.class)
//                    .toFuture()
//                    .get().getBody();
        Mono<InputStream> resp = this.apply(dataBufferFlux);
        ObjectMapper mapper = new ObjectMapper();
        try {
            data = mapper.readValue(resp.block(), VKData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //use object mapper to deserialize the resp.
        for (VKItem item : data.response.items) {
            if (item.attachments != null) {
                for (VKAttachment attach : item.attachments) {
                    if (attach.photo == null)
                        continue;
                    Optional<VKSize> vkSize = attach.photo.sizes.stream().max((a, b) -> a.width - b.width);
                    if (vkSize.isPresent()) {
                        {
                            result.add(new ImageDTO(vkSize.get().url,
                                    vkSize.get().getWidth() > vkSize.get().getHeight() ? "backdrop" : "poster", "VK"));
                        }
                    }
                }
            } else {
                Optional<VKSize> vkSize = item.sizes.stream().max((a, b) -> a.width - b.width);
                if (vkSize.isPresent()) {
                    {
                        result.add(new ImageDTO(vkSize.get().url,
                                vkSize.get().getWidth() > vkSize.get().getHeight() ? "backdrop" : "poster", "VK"));
                    }
                }
            }
        }
        return result;
    }
}
