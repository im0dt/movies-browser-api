package com.spark.movie.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = "\r\n";

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        return stringList != null ? String.join(SPLIT_CHAR, stringList) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        if( string == null)
            return Collections.emptyList();
        int count = string.split(SPLIT_CHAR).length > 5 ? 5 : string.split(SPLIT_CHAR).length;
        return string != null ? Arrays.asList(string.split(SPLIT_CHAR)).subList(0,count) : Collections.emptyList();
    }
}
