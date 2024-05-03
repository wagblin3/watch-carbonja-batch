package com.carbonwatch.carbonja.batch.dto.dtoMetric;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DimensionMap{

    @JsonProperty ("dt.entity.host.name")
    public String dtEntityHostName;
    @JsonProperty ("dt.entity.host")
    public String dtEntityHost;

    @JsonProperty ("dt.entity.cloud_application_namespace")
    public String dtEntityCloudApplicationNamespace;
    @JsonProperty ("dt.entity.cloud_application_namespace.name")
    public String dtEntityCloudApplicationNamespaceName;

    @JsonProperty ("dt.entity.process_group")
    public String dtEntityProcessGroup;
    @JsonProperty ("dt.entity.process_group.name")
    public String dtEntityProcessGroupName;

    @JsonProperty ("dt.entity.service.name")
    public String dtEntityServiceName;
    @JsonProperty ("dt.entity.service")
    public String dtEntityService;

    @JsonProperty ("dt.entity.cloud_application.name")
    public String dtEntityCloudApplicationName;
    @JsonProperty ("dt.entity.cloud_application")
    public String dtEntityCloudApplication;
    @JsonProperty ("dt.entity.kubernetes_cluster")
    public String dtEntityKubernetesCluster;
    @JsonProperty ("dt.entity.kubernetes_cluster.name")
    public String dtEntityKubernetesClusterName;

    public String getDtEntityHostName() {
        return dtEntityHostName;
    }

    public void setDtEntityHostName(String dtEntityHostName) {
        this.dtEntityHostName = dtEntityHostName;
    }

    public String getDtEntityHost() {
        return dtEntityHost;
    }

    public void setDtEntityHost(String dtEntityHost) {
        this.dtEntityHost = dtEntityHost;
    }

    public String getDtEntityCloudApplicationNamespace() {
        return dtEntityCloudApplicationNamespace;
    }

    public void setDtEntityCloudApplicationNamespace(String dtEntityCloudApplicationNamespace) {
        this.dtEntityCloudApplicationNamespace = dtEntityCloudApplicationNamespace;
    }

    public String getDtEntityCloudApplicationNamespaceName() {
        return dtEntityCloudApplicationNamespaceName;
    }

    public void setDtEntityCloudApplicationNamespaceName(String dtEntityCloudApplicationNamespaceName) {
        this.dtEntityCloudApplicationNamespaceName = dtEntityCloudApplicationNamespaceName;
    }

    public String getDtEntityProcessGroup() {
        return dtEntityProcessGroup;
    }

    public void setDtEntityProcessGroup(String dtEntityProcessGroup) {
        this.dtEntityProcessGroup = dtEntityProcessGroup;
    }

    public String getDtEntityProcessGroupName() {
        return dtEntityProcessGroupName;
    }

    public void setDtEntityProcessGroupName(String dtEntityProcessGroupName) {
        this.dtEntityProcessGroupName = dtEntityProcessGroupName;
    }

    public String getDtEntityServiceName() {
        return dtEntityServiceName;
    }

    public void setDtEntityServiceName(String dtEntityServiceName) {
        this.dtEntityServiceName = dtEntityServiceName;
    }

    public String getDtEntityService() {
        return dtEntityService;
    }

    public void setDtEntityService(String dtEntityService) {
        this.dtEntityService = dtEntityService;
    }

    public String getDtEntityCloudApplicationName() {
        return dtEntityCloudApplicationName;
    }

    public void setDtEntityCloudApplicationName(String dtEntityCloudApplicationName) {
        this.dtEntityCloudApplicationName = dtEntityCloudApplicationName;
    }

    public String getDtEntityCloudApplication() {
        return dtEntityCloudApplication;
    }

    public void setDtEntityCloudApplication(String dtEntityCloudApplication) {
        this.dtEntityCloudApplication = dtEntityCloudApplication;
    }

    public String getDtEntityKubernetesCluster() {
        return dtEntityKubernetesCluster;
    }

    public void setDtEntityKubernetesCluster(String dtEntityKubernetesCluster) {
        this.dtEntityKubernetesCluster = dtEntityKubernetesCluster;
    }

    public String getDtEntityKubernetesClusterName() {
        return dtEntityKubernetesClusterName;
    }

    public void setDtEntityKubernetesClusterName(String dtEntityKubernetesClusterName) {
        this.dtEntityKubernetesClusterName = dtEntityKubernetesClusterName;
    }
}