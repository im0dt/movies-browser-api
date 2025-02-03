package com.spark.movie.ppics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpicsSearchReponse {
    public  String detail;
    public PpicsResult results;
}
