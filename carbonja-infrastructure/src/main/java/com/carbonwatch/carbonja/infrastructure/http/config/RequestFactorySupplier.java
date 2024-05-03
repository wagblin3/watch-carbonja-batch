package com.carbonwatch.carbonja.infrastructure.http.config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Optional;
import java.util.function.Supplier;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.carbonwatch.carbonja.domain.common.exception.TechnicalException;

/**
 * RequestFactory Supplier.
 */
public class RequestFactorySupplier implements Supplier<ClientHttpRequestFactory> {

    private final ClientProperties clientProperties;

    private final Optional<SSLSocketFactory> sslSocketFactory;

    public RequestFactorySupplier(final ClientProperties clientProperties, final SSLSocketFactory sslSocketFactory) {
        this.clientProperties = clientProperties;
        this.sslSocketFactory = Optional.ofNullable(sslSocketFactory);
    }

    @Override
    public ClientHttpRequestFactory get() {
        final SimpleClientHttpRequestFactory requestFactory;
        if (sslSocketFactory.isPresent()) {
            requestFactory = new HttpsClientRequestFactory(sslSocketFactory.get());
        } else {
            requestFactory = new SimpleClientHttpRequestFactory();
        }

        // Use the proxy if defined
        if (clientProperties.isProxyDefined()) {
            final Proxy proxy = new Proxy(Type.HTTP,
                    new InetSocketAddress(clientProperties.getProxy().getHostName(), clientProperties.getProxy().getHostPort()));
            requestFactory.setProxy(proxy);
        }
        // using BufferingClientHttpRequestFactory allows to read the response more than once - Necessary for debugging with LoggingHTTPInterceptor.
        return new BufferingClientHttpRequestFactory(requestFactory);
    }

    /**
     * Extends ClientHttpRequestFactory for enabling SSL
     */
    static class HttpsClientRequestFactory extends SimpleClientHttpRequestFactory {
        private final SSLSocketFactory sslSocketFactory;

        public HttpsClientRequestFactory(final SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
        }

        @Override
        protected void prepareConnection(final HttpURLConnection connection, final String httpMethod) throws IOException {
            if (connection instanceof HttpsURLConnection) {
                prepareHttpsConnection((HttpsURLConnection) connection);
            }
            super.prepareConnection(connection, httpMethod);
        }

        private void prepareHttpsConnection(final HttpsURLConnection connection) {
            try {
                connection.setSSLSocketFactory(sslSocketFactory);
            } catch (final Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}