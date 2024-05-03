package com.carbonwatch.carbonja.batch.common;

import com.carbonwatch.carbonja.batch.config.ApplicationProps;
import com.carbonwatch.carbonja.batch.dto.dtoMetric.RootMetric;
import com.carbonwatch.carbonja.batch.dto.dtoProperties.RootProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@Configuration
public class ItemReader {

    private final WebClient oneWebClient; private final WebClient twoWebClient; private final ApplicationProps applicationProps;
    public ItemReader(@Qualifier("oneWebClient") final WebClient oneWebClient,
                      @Qualifier("twoWebClient") final WebClient twoWebClient,
                      final ApplicationProps applicationProps) {
        this.oneWebClient = oneWebClient;
        this.twoWebClient = twoWebClient;
        this.applicationProps = applicationProps;
    }

    public RootMetric readMetric(String cluster, String metricSelector, String mzSelector) {

        WebClient webClient;

        if (Objects.equals(cluster, "One")) { webClient = oneWebClient; } else { webClient = twoWebClient;}

        RootMetric responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v2/metrics/query")
                        .queryParam("metricSelector", metricSelector)
                        .queryParam("resolution", "1h")
                        .queryParam("from", applicationProps.getFrom()) // a externaliser dans un COS ou Base pour garder le dernier point de collecte ?
                        .queryParam("to", applicationProps.getTo())
                        .queryParam("mzSelector", mzSelector)
                        .build()
                )
                .retrieve()
                .toEntity(RootMetric.class)
                .block()
                .getBody();

        return responseMono;
    }

    public RootProperties readProperties(String cluster, String entityHost, String propertieFields) {

        WebClient webClient;

        if ( cluster == "One") { webClient = oneWebClient; } else { webClient = twoWebClient;}

        RootProperties responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v2/entities/{param}")
                        .queryParam("fields", propertieFields)
                        .build(entityHost))
                .retrieve()
                .toEntity(RootProperties.class)
                .block()
                .getBody();

        return responseMono;
    }



}
