package com.carbonwatch.carbonja.batch.job;

import com.carbonwatch.carbonja.batch.config.ApplicationProps;
import com.carbonwatch.carbonja.batch.tasklet.ServiceTasklet;
import com.carbonwatch.carbonja.batch.listener.AbstractModularJob;
import com.carbonwatch.carbonja.batch.common.ItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceJobConfig extends AbstractModularJob {

    protected static final String JOB_NAME = "service-job";

    @Autowired
    @Qualifier ("oneWebClient")
    WebClient oneWebClient;

    @Autowired
    @Qualifier ("twoWebClient")
    WebClient twoWebClient;

    @Autowired
    private ApplicationProps applicationProps;

    @Bean
    Job serviceJob(Step serviceStep, JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(serviceStep)
                .listener(reportListener())
                .build();
    }

    @Bean
    Step serviceStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("service-step", jobRepository)
                .tasklet(serviceTasklet(), transactionManager)
                .build();
    }

    @Bean
    @JobScope
    ServiceTasklet serviceTasklet (){
        return new ServiceTasklet(
                new ItemReader(oneWebClient, twoWebClient, applicationProps)
        );
    }
}
