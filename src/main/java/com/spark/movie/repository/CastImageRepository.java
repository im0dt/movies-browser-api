package com.spark.movie.repository;

import com.spark.movie.model.CastImage;
import com.spark.movie.model.Movie;
import com.spark.movie.model.MovieImage;
import com.spark.movie.model.MovieLanguage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CastImageRepository extends JpaRepository<CastImage, Integer> {

    @Query("SELECT ci FROM CastImage ci where trim(ci.name)=?1 and ci.deleted = 0")
    List<CastImage> findByName(String name);

    @Query("SELECT ci FROM CastImage ci WHERE ci.name = ?1 and ci.type= ?2 and ci.url=?3")
    CastImage findByUrl(String name, String type, String url);
}
