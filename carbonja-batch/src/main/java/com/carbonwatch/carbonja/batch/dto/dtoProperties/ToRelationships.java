package com.carbonwatch.carbonja.batch.dto.dtoProperties;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToRelationships{

    public ArrayList<IsInstanceOf> isInstanceOf;
    public ArrayList<IsClusterOfNamespace> isClusterOfNamespace;

    public ArrayList<IsClusterOfNamespace> getIsClusterOfNamespace() {
        return isClusterOfNamespace;
    }

    public void setIsClusterOfNamespace(ArrayList<IsClusterOfNamespace> isClusterOfNamespace) {
        this.isClusterOfNamespace = isClusterOfNamespace;
    }

    public ArrayList<IsInstanceOf> getIsInstanceOf() {
        return isInstanceOf;
    }

    public void setIsInstanceOf(ArrayList<IsInstanceOf> isInstanceOf) {
        this.isInstanceOf = isInstanceOf;
    }
}
