package com.spark.movie.ppics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpicsPosts {
    public String gallery_url;
    public String image_url;
}
