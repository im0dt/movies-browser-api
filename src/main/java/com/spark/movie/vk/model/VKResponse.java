package com.spark.movie.vk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VKResponse{
    public List<VKItem> items;
    public int count;
}
