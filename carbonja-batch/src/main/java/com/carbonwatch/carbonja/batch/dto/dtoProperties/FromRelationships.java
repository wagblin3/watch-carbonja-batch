package com.carbonwatch.carbonja.batch.dto.dtoProperties;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FromRelationships {

    public ArrayList<IsServiceOf> isServiceOf;

    public ArrayList<IsClusterOfHost> isClusterOfHost;

    public ArrayList<RunsOn> runsOn;

    public ArrayList<IsClusterOfHost> getIsClusterOfHost() {
        return isClusterOfHost;
    }

    public void setIsClusterOfHost(ArrayList<IsClusterOfHost> isClusterOfHost) {
        this.isClusterOfHost = isClusterOfHost;
    }

    public ArrayList<RunsOn> getRunsOn() {
        return runsOn;
    }

    public void setRunsOn(ArrayList<RunsOn> runsOn) {
        this.runsOn = runsOn;
    }
}