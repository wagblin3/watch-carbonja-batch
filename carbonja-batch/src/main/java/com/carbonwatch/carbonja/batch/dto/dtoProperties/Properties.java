package com.carbonwatch.carbonja.batch.dto.dtoProperties;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Properties{
    public int logicalCpuCores;
    public ArrayList<CustomHostMetadatum> customHostMetadata;
    public ArrayList<AdditionalSystemInfo> additionalSystemInfo;
    public String osVersion;
    public int cpuCores;
    public Object memoryTotal;
    public String osType;
    public Object physicalMemory;

    public int getLogicalCpuCores() {
        return logicalCpuCores;
    }

    public void setLogicalCpuCores(int logicalCpuCores) {
        this.logicalCpuCores = logicalCpuCores;
    }

    public ArrayList<CustomHostMetadatum> getCustomHostMetadata() {
        return customHostMetadata;
    }

    public void setCustomHostMetadata(ArrayList<CustomHostMetadatum> customHostMetadata) {
        this.customHostMetadata = customHostMetadata;
    }

    public ArrayList<AdditionalSystemInfo> getAdditionalSystemInfo() {
        return additionalSystemInfo;
    }

    public void setAdditionalSystemInfo(ArrayList<AdditionalSystemInfo> additionalSystemInfo) {
        this.additionalSystemInfo = additionalSystemInfo;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public int getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }

    public Object getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(Object memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public Object getPhysicalMemory() {
        return physicalMemory;
    }

    public void setPhysicalMemory(Object physicalMemory) {
        this.physicalMemory = physicalMemory;
    }
}
