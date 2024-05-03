package com.carbonwatch.carbonja.infrastructure.http.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;

/**
 * Interceptor who log HTTP outgoing requests and incoming responses.
 *
 * <blockquote> Note : Must be used with a BufferingClientHttpRequestFactory (cf {@link RequestFactorySupplier} )</blockquote>
 */
public class LoggingHTTPInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingHTTPInterceptor.class);

    private static final String SEPARATORS = "================================================";
    private static final String REQUEST_BEGIN = SEPARATORS + StringUtils.center("request begin", 16) + SEPARATORS;
    private static final String REQUEST_END = SEPARATORS + StringUtils.center("request end", 16) + SEPARATORS;
    private static final String RESPONSE_BEGIN = SEPARATORS + StringUtils.center("response begin", 16) + SEPARATORS;
    private static final String RESPONSE_END = SEPARATORS + StringUtils.center("response end", 16) + SEPARATORS;

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException {
        final StopWatch watch = new StopWatch();
        watch.start();
        if (log.isTraceEnabled()) {
            traceRequest(request, body);
        }
        final ClientHttpResponse response = execution.execute(request, body);
        watch.stop();
        if (log.isTraceEnabled()) {
            traceResponse(response, watch.getLastTaskTimeMillis());
        }
        return response;
    }

    private void traceRequest(final HttpRequest request, final byte[] body) {
        log.trace(REQUEST_BEGIN);
        log.trace("URI         : {}", request.getURI());
        log.trace("Method      : {}", request.getMethod());
        log.trace("Headers     : {}", request.getHeaders());
        final String bodyAsString = new String(body, StandardCharsets.UTF_8);
        log.trace("Request body: {}", bodyAsString);
        log.trace(REQUEST_END);
    }

    /**
     * Trace the response without destroying the body (using BufferedReader)
     *
     * @param durationTime
     */
    private void traceResponse(final ClientHttpResponse response, final long durationTime) throws IOException {
        log.trace(RESPONSE_BEGIN);
        log.trace("Status code  : {}", response.getStatusCode());
        log.trace("Status text  : {}", response.getStatusText());
        log.trace("Headers      : {}", response.getHeaders());
        final InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
        final String body = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
        log.trace("Response body: {}", body);
        log.trace("Duration     : {} ms", durationTime);
        log.trace(RESPONSE_END);
    }

}