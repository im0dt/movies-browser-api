package com.spark.movie.vk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VKAttachment{
    public String type;
    public VKPhoto photo;
}
