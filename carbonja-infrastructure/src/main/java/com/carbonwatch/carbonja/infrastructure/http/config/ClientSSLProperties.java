package com.carbonwatch.carbonja.infrastructure.http.config;

import java.security.KeyStore;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("client.ssl")
public class ClientSSLProperties {

	private static final String DEFAULT_TLS_VERSION = "TLSv1.2";

	private boolean enabled = false;

	private String keyStore;

	// Typically "jks" or "pkcs12"
	private String keyStoreType = KeyStore.getDefaultType();

	private char[] keyStorePassword;

	private String trustStore;

	// Typically "jks" or "pkcs12"
	private String trustStoreType = KeyStore.getDefaultType();

	private char[] trustStorePassword;

	private String tlsVersion = DEFAULT_TLS_VERSION;
	
	/**
	 * @return true if keystore path is defined
	 */
	public boolean isKeyStoreDefined() {
		return StringUtils.isNotBlank(keyStore);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public String getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(final String keyStore) {
		this.keyStore = keyStore;
	}

	public String getKeyStoreType() {
		return keyStoreType;
	}

	public void setKeyStoreType(final String keyStoreType) {
		this.keyStoreType = keyStoreType;
	}

	public char[] getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(final String keyStorePassword) {
		this.keyStorePassword = keyStorePassword.toCharArray();
	}

	public String getTrustStore() {
		return trustStore;
	}

	public void setTrustStore(final String trustStore) {
		this.trustStore = trustStore;
	}

	public String getTrustStoreType() {
		return trustStoreType;
	}

	public void setTrustStoreType(final String trustStoreType) {
		this.trustStoreType = trustStoreType;
	}

	public char[] getTrustStorePassword() {
		return trustStorePassword;
	}

	public void setTrustStorePassword(final String trustStorePassword) {
		this.trustStorePassword = trustStorePassword.toCharArray();
	}

	public String getTlsVersion() {
		return tlsVersion;
	}

	public void setTlsVersion(final String tlsVersion) {
		this.tlsVersion = tlsVersion;
	}

}