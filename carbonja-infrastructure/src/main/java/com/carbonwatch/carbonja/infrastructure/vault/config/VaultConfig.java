
package com.carbonwatch.carbonja.infrastructure.vault.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.vault.config.SecretBackendConfigurer;
import org.springframework.cloud.vault.config.VaultConfigurer;

public class VaultConfig implements VaultConfigurer {

    @Autowired(required = true)
    private VaultCustomProperties properties;

    @Override
    public void addSecretBackends(final SecretBackendConfigurer configurer) {
        properties.getPaths().forEach(path -> configurer.add(path));

        configurer.registerDefaultKeyValueSecretBackends(false);
        configurer.registerDefaultDiscoveredSecretBackends(true);
    }
}
