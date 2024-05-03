
package com.carbonwatch.carbonja.infrastructure.vault.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * Aspect used to add retry functionality on Vault
 */
@ConditionalOnProperty(prefix = "spring.cloud.vault.retry", name = "enabled", havingValue = "true", matchIfMissing = false)
@Component
@Aspect
public class RetryVaultAspect {

    private static Logger log = LoggerFactory.getLogger(RetryVaultAspect.class);

    static {
        log.info("ASPECT RetryVaultAspect ACTIF");
    }

    @Autowired
    private RetryTemplate retryTemplate;

    @Pointcut("execution(* org.springframework.vault.core.VaultTemplate.read(..))")
    public void vaultTemplateReadMethods() {
        // Pointcut's methods doesn't have body
    }

    @Around("vaultTemplateReadMethods()")
    public Object aroundServiceMethods(final ProceedingJoinPoint joinPoint) {
        try {
            return retryTemplate.execute(retryContext -> joinPoint.proceed());
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
