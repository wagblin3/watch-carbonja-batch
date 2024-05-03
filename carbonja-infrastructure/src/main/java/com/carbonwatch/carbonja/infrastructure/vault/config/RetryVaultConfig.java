
package com.carbonwatch.carbonja.infrastructure.vault.config;

import static org.springframework.util.Assert.notNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * This config create a RestTemplateCustomizer that be able to retry Vault API calls.
 *
 * <b>Don't forget to add it in the spring.factories META_INF file</b>
 *
 * Using {@link VaultCustomProperties.Retry} properties To configure retry :
 *
 * <pre class="code">
 * spring.cloud.vault.retry.initialInterval (Initial retry interval in milliseconds. default 1000)
 * spring.cloud.vault.retry.multiplier (Multiplier for next interval. default 1.1 )
 * spring.cloud.vault.retry.maxInterval (Maximum interval for backoff; default 2000)
 * spring.cloud.vault.retry.maxAttempts (Maximum number of attempts. default 6)
 * </pre>
 */
public class RetryVaultConfig {

    @Autowired(required = true)
    private VaultCustomProperties properties;

    @ConditionalOnProperty(prefix = "spring.cloud.vault.retry", name = "enabled", havingValue = "true", matchIfMissing = false)
    @Bean
    RetryTemplate retryTemplate() {
        return buildRetryTemplate(properties.getRetry());
    }

    static RetryTemplate buildRetryTemplate(final VaultCustomProperties.Retry retryProperties) {
        notNull(retryProperties, "a VaultCustomProperties.Retry is required");
        final RetryTemplate retryTemplate = new RetryTemplate();

        final ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(retryProperties.getInitialInterval());
        policy.setMultiplier(retryProperties.getMultiplier());
        policy.setMaxInterval(retryProperties.getMaxInterval());

        retryTemplate.setBackOffPolicy(policy);
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(retryProperties.getMaxAttempts()));
        return retryTemplate;
    }

}
