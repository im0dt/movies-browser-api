package com.spark.movie.ppics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpicsResult {
    public List<PpicsPosts> posts;
}
