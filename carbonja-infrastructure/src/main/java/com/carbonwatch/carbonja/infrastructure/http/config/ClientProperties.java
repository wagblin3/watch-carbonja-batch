package com.carbonwatch.carbonja.infrastructure.http.config;

import java.time.Duration;

import org.apache.commons.lang3.StringUtils;

/**
 * Extends this class to have specialized properties for each http client.
 */
public abstract class ClientProperties {

    // populated with {client}.url property
    private String url;

    private final TimeOut timeout = new TimeOut();

    private final Proxy proxy = new Proxy();

    private final BasicAuthorization basicAuthorization = new BasicAuthorization();

    public boolean isProxyDefined() {
        return StringUtils.isNotBlank(proxy.getHostName()) && proxy.getHostPort() > 0;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * @return the timeout
     */
    public TimeOut getTimeout() {
        return timeout;
    }

    /**
     * @return the proxy
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * @return the basicAuthorization
     */
    public BasicAuthorization getBasicAuthorization() {
        return basicAuthorization;
    }

    /**
     * Related TimeOut properties
     */
    public static class TimeOut {

        // populated with {client}.timeout.connect property
        private Duration connect;

        // populated with {client}.timeout.read property
        private Duration read;

        /**
         * @return the connect
         */
        public Duration getConnect() {
            return connect;
        }

        /**
         * @param connect
         *            the connect to set
         */
        public void setConnect(final Duration connect) {
            this.connect = connect;
        }

        /**
         * @return the read
         */
        public Duration getRead() {
            return read;
        }

        /**
         * @param read
         *            the read to set
         */
        public void setRead(final Duration read) {
            this.read = read;
        }

    }

    /**
     * Related Proxy properties
     */
    public static class Proxy {

        // populated with {client}.proxy.host.name property
        private String hostName;

        // populated with {client}.proxy.host.port property
        private int hostPort;

        /**
         * @return the hostName
         */
        public String getHostName() {
            return hostName;
        }

        /**
         * @param hostName
         *            the hostName to set
         */
        public void setHostName(final String hostName) {
            this.hostName = hostName;
        }

        /**
         * @return the hostPort
         */
        public int getHostPort() {
            return hostPort;
        }

        /**
         * @param hostPort
         *            the hostPort to set
         */
        public void setHostPort(final int hostPort) {
            this.hostPort = hostPort;
        }

    }

    /**
     * Related BasicAuthorization properties
     */
    public static class BasicAuthorization {

        // populated with {client}.basicAuthorization.enabled property
        private Boolean enabled = false;

        // populated with {client}.basicAuthorization.user property
        private String user;

        // populated with {client}.basicAuthorization.password property
        private String password;

        /**
         * @return the enabled
         */
        public Boolean getEnabled() {
            return enabled;
        }

        /**
         * @param enabled
         *            the enabled to set
         */
        public void setEnabled(final Boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * @return the user
         */
        public String getUser() {
            return user;
        }

        /**
         * @param user
         *            the user to set
         */
        public void setUser(final String user) {
            this.user = user;
        }

        /**
         * @return the password
         */
        public String getPassword() {
            return password;
        }

        /**
         * @param password
         *            the password to set
         */
        public void setPassword(final String password) {
            this.password = password;
        }

    }

}
