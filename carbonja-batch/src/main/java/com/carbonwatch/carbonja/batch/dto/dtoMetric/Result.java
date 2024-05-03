package com.carbonwatch.carbonja.batch.dto.dtoMetric;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result{

    public String metricId;
    public double dataPointCountRatio;
    public double dimensionCountRatio;
    public ArrayList<Datum> data;

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public double getDataPointCountRatio() {
        return dataPointCountRatio;
    }

    public void setDataPointCountRatio(double dataPointCountRatio) {
        this.dataPointCountRatio = dataPointCountRatio;
    }

    public double getDimensionCountRatio() {
        return dimensionCountRatio;
    }

    public void setDimensionCountRatio(double dimensionCountRatio) {
        this.dimensionCountRatio = dimensionCountRatio;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }
}