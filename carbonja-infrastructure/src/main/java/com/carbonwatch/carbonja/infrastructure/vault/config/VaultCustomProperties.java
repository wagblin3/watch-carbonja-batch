
package com.carbonwatch.carbonja.infrastructure.vault.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.cloud.vault")
public class VaultCustomProperties {

    private final List<String> paths = new ArrayList<>();

    private Retry retry = new Retry();

    public List<String> getPaths() {
        return paths;
    }

    public Retry getRetry() {
        return this.retry;
    }

    public void setRetry(final Retry retry) {
        this.retry = retry;
    }

    /**
     * Vault retry configuration
     */
    public static class Retry {

        boolean enabled = false;

        /**
         * Initial retry interval in milliseconds.
         */
        long initialInterval = 1000;

        /**
         * Multiplier for next interval.
         */
        double multiplier = 1.1;

        /**
         * Maximum interval for backoff.
         */
        long maxInterval = 2000;

        /**
         * Maximum number of attempts.
         */
        int maxAttempts = 6;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }

        public long getInitialInterval() {
            return initialInterval;
        }

        public void setInitialInterval(final long initialInterval) {
            this.initialInterval = initialInterval;
        }

        public double getMultiplier() {
            return multiplier;
        }

        public void setMultiplier(final double multiplier) {
            this.multiplier = multiplier;
        }

        public long getMaxInterval() {
            return maxInterval;
        }

        public void setMaxInterval(final long maxInterval) {
            this.maxInterval = maxInterval;
        }

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(final int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

    }
}