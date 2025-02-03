package com.spark.movie.ppics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpicsGalleryResponse {
    public PpicsUrls urls;
}
