package com.spark.movie.vk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VKItem{

    public List<VKAttachment> attachments;
    public List<VKSize> sizes;
}
