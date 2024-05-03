package com.carbonwatch.carbonja.batch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private ApplicationProps applicationProps;

    @Bean (name = "oneWebClientBuilder")
    public WebClient.Builder oneWebClientBuilder() {
        return WebClient.builder()
                .baseUrl(applicationProps.getApiurlone())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Api-Token "+applicationProps.getApitokenone())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(10000 * 1024))
                        .build());
    }
    @Bean (name = "oneWebClient")
    public WebClient oneWebClient(@Qualifier("oneWebClientBuilder") WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }

    @Bean (name = "twoWebClientBuilder")
    public WebClient.Builder twoWebClientBuilder() {
        return WebClient.builder()
                .baseUrl(applicationProps.getApiurltwo())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Api-Token "+applicationProps.getApitokentwo())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(10000 * 1024))
                        .build());
    }
    @Bean (name = "twoWebClient")
    public WebClient twoWebClient(@Qualifier("twoWebClientBuilder") WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }

}
