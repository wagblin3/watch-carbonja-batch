package com.carbonwatch.carbonja.batch.listener;

import org.springframework.context.annotation.Bean;

public abstract class AbstractModularJob {

    /**
     * Base configuration for all jobs (ie define Listener used for all jobs)
     */

    protected AbstractModularJob() {
        super();
    }

    @Bean
    protected JobReportListener reportListener() {
        return new JobReportListener();
    }

}
