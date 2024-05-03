package com.carbonwatch.carbonja.batch.dto.dtoProperties;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RootProperties {
    public String entityId;
    public String type;
    public String displayName;
    public Object firstSeenTms;
    public Properties properties;
    public ArrayList<Tag> tags;
    public ToRelationships toRelationships;
    public ArrayList<ManagementZone> managementZones;
    public FromRelationships fromRelationships;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getFirstSeenTms() {
        return firstSeenTms;
    }

    public void setFirstSeenTms(Object firstSeenTms) {
        this.firstSeenTms = firstSeenTms;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ToRelationships getToRelationships() {
        return toRelationships;
    }

    public void setToRelationships(ToRelationships toRelationships) {
        this.toRelationships = toRelationships;
    }

    public ArrayList<ManagementZone> getManagementZones() {
        return managementZones;
    }

    public void setManagementZones(ArrayList<ManagementZone> managementZones) {
        this.managementZones = managementZones;
    }

    public FromRelationships getFromRelationships() {
        return fromRelationships;
    }

    public void setFromRelationships(FromRelationships fromRelationships) {
        this.fromRelationships = fromRelationships;
    }
}

