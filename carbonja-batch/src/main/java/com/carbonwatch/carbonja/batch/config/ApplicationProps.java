package com.carbonwatch.carbonja.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "greeneco")
public class ApplicationProps {
    private String from;
    private String to;
    private String name;
    private String apiurlone;
    private String apitokenone;
    private String apiurltwo;
    private String apitokentwo;
    private List<String> mzone;
    private List<String> mztwo;

    public String getFrom() {return from;}
    public void setFrom(String from) {this.from = from;}
    public String getTo() {return to;}
    public void setTo(String to) {this.to = to;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getApiurlone() {return apiurlone;}
    public void setApiurlone(String apiurlone) {this.apiurlone = apiurlone;}
    public String getApitokenone() {return apitokenone;}
    public void setApitokenone(String apitokenone) {this.apitokenone = apitokenone;}
    public String getApiurltwo() {return apiurltwo;}
    public void setApiurltwo(String apiurltwo) {this.apiurltwo = apiurltwo;}
    public String getApitokentwo() {return apitokentwo;}
    public void setApitokentwo(String apitokentwo) {this.apitokentwo = apitokentwo;}
    public List<String> getMzone() {return mzone;}
    public void setMzone(List<String> mzone) {this.mzone = mzone;}
    public List<String> getMztwo() {return mztwo;}
    public void setMztwo(List<String> mztwo) {this.mztwo = mztwo;}
}