package com.spark.movie.service;

import com.spark.movie.dto.MovieDTO;
import com.spark.movie.dto.ImageDTO;
import com.spark.movie.dto.MovieImageMapper;
import com.spark.movie.model.*;
import com.spark.movie.ppics.service.PpicsService;
import com.spark.movie.repository.CastImageRepository;
import com.spark.movie.repository.MovieRepository;
import com.spark.movie.tmdb.service.THMDBService;
import com.spark.movie.vk.service.VKService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CastService {
    private final MovieRepository movieRepository;
    private final CastImageRepository castImageRepository;
    @Autowired
    private final THMDBService thmdbService;
    @Autowired
    private final VKService vkService;
    @Autowired
    private final PpicsService ppicsService;
    @Autowired
    private final MovieImageMapper movieImageMapper;

    public CastService(MovieRepository movieRepository,
                       CastImageRepository castImageRepository,
                       THMDBService thmdbService,
                       VKService vkService,
                       MovieImageMapper movieImageMapper,
                       PpicsService ppicsService) {
        this.movieRepository = movieRepository;
        this.castImageRepository = castImageRepository;
        this.thmdbService = thmdbService;
        this.vkService = vkService;
        this.movieImageMapper = movieImageMapper;
        this.ppicsService = ppicsService;
    }

    public List<Cast> listAllCastes(int level){
        List<Movie> movies = movieRepository.findAll(level);
        List<Cast> casts = movies.stream().flatMap(x -> x.getCast().stream()).filter(x->x!=null && !x.isEmpty())
                .collect(groupingBy(x-> x)).keySet().stream()
                .map(x-> new Cast(x, movies.stream().filter(c->c.getCast().contains(x)).map(m-> new MovieDTO(m.getId(), m.getName())).toList())
                ).collect(Collectors.toList());

        for (int i = 0; i < casts.size(); i++) {
            Cast cast = casts.get(i);
            List<CastImage> images = castImageRepository.findByName(cast.getName());
            if (!images.isEmpty() && images.get(0).getAdult() == 1 && images.get(0).getCastGender() != 1) {
                casts.remove(cast);
                i--;
                continue;
            }
            cast.setImages(images.stream().map(x -> new CastImage(x.getId() ,x.getName(), x.getUrl(), x.getType(), x.getSource())).collect(Collectors.toList()));
        }
        return casts;
    }

    public Cast getCastByName(String name) {
        Cast cast = new Cast(name, new ArrayList<>());
        List<CastImage> images = castImageRepository.findByName(cast.getName());
        cast.setImages(images.stream().map(x -> new CastImage(x.getId() ,x.getName(), x.getUrl(), x.getType(), x.getSource())).collect(Collectors.toList()));
        return cast;
    }

    @Transactional
    public List<CastImage> extractCastImages(int level, String name) {
        List<CastImage> result = new ArrayList<>();
            for (CastImage img : thmdbService.extractCastImage(level, name)) {
                if (castImageRepository.findByUrl(name, img.getType(), img.getUrl()) == null) {
                    result.add(img);
                }
            }
        castImageRepository.saveAllAndFlush(result);
        return result;
    }

    public List<ImageDTO> extractVKCastImage(String opId, String name, String id, String ownerId, String apiKey, int offset, int count){
        List<CastImage> result = new ArrayList<>();
        List<ImageDTO> res = new ArrayList<>();
        res = vkService.extractImages(apiKey, opId, id, ownerId, offset, count);
        for (ImageDTO img : res) {
            if (castImageRepository.findByUrl(name, img.getType(), img.getUrl()) == null) {
                CastImage castImage = new CastImage();
                movieImageMapper.mapToCastImage(img, castImage);
                castImage.setName(name);
                castImage.setAdult(1);
                castImage.setCastGender(1);
                result.add(castImage);
            }
        }
        castImageRepository.saveAllAndFlush(result);
        return res;
    }

    public void deleteCastImage(int id){
        CastImage ci = castImageRepository.findById(id).get();
        ci.setDeleted(1);
        castImageRepository.saveAndFlush(ci);
    }

    public void updateCastImageType(Integer id, String type) {
        CastImage castImage = castImageRepository.findById(id).get();
        castImage.setType(type);
        castImageRepository.saveAndFlush(castImage);
    }

    public void createCastImage(ImageDTO imageDTO) {
        CastImage castImage = new CastImage();
        movieImageMapper.mapToCastImage(imageDTO, castImage);
        castImage.setName(imageDTO.getObjectId());
        castImage.setAdult(1);
        castImage.setCastGender(1);
        castImageRepository.saveAndFlush(castImage);
    }

    public List<ImageDTO> extractPpicsCastImage(String name) {
        List<CastImage> result = new ArrayList<>();
        List<ImageDTO> res = new ArrayList<>();
        res = ppicsService.extractImages(name);
        for (ImageDTO img : res) {
            if (castImageRepository.findByUrl(name, img.getType(), img.getUrl()) == null) {
                CastImage castImage = new CastImage();
                movieImageMapper.mapToCastImage(img, castImage);
                castImage.setName(name);
                castImage.setAdult(1);
                castImage.setCastGender(1);
                result.add(castImage);
            }
        }
        castImageRepository.saveAllAndFlush(result);
        return res;
    }
}
