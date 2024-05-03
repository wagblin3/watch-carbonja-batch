package com.carbonwatch.carbonja.batch.dto.dtoMetric;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Datum{

    public ArrayList<String> dimensions;
    public DimensionMap dimensionMap;
    public ArrayList<Object> timestamps;
    public ArrayList<Object> values;

    public ArrayList<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(ArrayList<String> dimensions) {
        this.dimensions = dimensions;
    }

    public DimensionMap getDimensionMap() {
        return dimensionMap;
    }

    public void setDimensionMap(DimensionMap dimensionMap) {
        this.dimensionMap = dimensionMap;
    }

    public ArrayList<Object> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(ArrayList<Object> timestamps) {
        this.timestamps = timestamps;
    }

    public ArrayList<Object> getValues() {
        return values;
    }

    public void setValues(ArrayList<Object> values) {
        this.values = values;
    }
}