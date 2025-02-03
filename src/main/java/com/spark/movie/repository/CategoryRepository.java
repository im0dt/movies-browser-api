package com.spark.movie.repository;

import com.spark.movie.model.Category;
import com.spark.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c where c.level=?1")
    List<Category> findAll(Integer level);

}
