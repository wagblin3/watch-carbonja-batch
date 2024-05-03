package com.carbonwatch.carbonja.batch.dto.dtoMetric;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RootMetric {

    public int totalCount;
    public Object nextPageKey;
    public String resolution;
//    @JsonProperty ("result")
    public ArrayList<Result> result;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Object getNextPageKey() {
        return nextPageKey;
    }

    public void setNextPageKey(Object nextPageKey) {
        this.nextPageKey = nextPageKey;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
        this.result = result;
    }
}

