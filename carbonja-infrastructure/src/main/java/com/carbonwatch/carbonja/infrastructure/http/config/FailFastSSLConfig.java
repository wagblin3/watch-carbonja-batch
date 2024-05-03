package com.carbonwatch.carbonja.infrastructure.http.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Profile;

@Profile({ "local", "cloud" })
@Configuration
public class FailFastSSLConfig {

	private static final Logger LOG = LoggerFactory.getLogger(FailFastSSLConfig.class);

	private static final String STARS = "*";

	@Bean
	@ConditionalOnProperty(name = "client.ssl.enabled")
	SSLSocketFactory buildSSLSocketFactory(final ClientSSLProperties ssl) throws GeneralSecurityException, IOException {

		final SSLContext sslContext = SSLContext.getInstance(ssl.getTlsVersion());

		final TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		final KeyStore trustStore = loadStore("TrustStore", ssl.getTrustStoreType(), ssl.getTrustStore(),
				ssl.getTrustStorePassword());
		trustManagerFactory.init(trustStore);

		if (ssl.isKeyStoreDefined()) {
			final KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			final KeyStore keyStore = loadStore("KeyStore", ssl.getKeyStoreType(), ssl.getKeyStore(),
					ssl.getKeyStorePassword());
			keyManagerFactory.init(keyStore, ssl.getKeyStorePassword());
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
					new java.security.SecureRandom());
		} else {
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new java.security.SecureRandom());
		}

		return sslContext.getSocketFactory();
	}

	/**
	 * Load the KeyStore or TrustStore
	 *
	 * @param label    used for logging
	 * @param type     keystore type (typically pkcs12 or JKS)
	 * @param filePath full path of the keystore
	 * @param password of the keystore
	 * @return the KeyStore object
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	private KeyStore loadStore(final String label, final String type, final String filePath, final char[] password)
			throws GeneralSecurityException, IOException {

		final String maskedPassword = StringUtils.repeat(STARS, password.length);
		LOG.info("Loading {} Type:{}, Path:{}, Password:{}", label, type, filePath, maskedPassword);

		final KeyStore keyStore = KeyStore.getInstance(type);
		try (InputStream stream = new FileInputStream(filePath)) {
			keyStore.load(stream, password);
		}
		return keyStore;
	}

}