package com.carbonwatch.carbonja.batch.job;

import com.carbonwatch.carbonja.batch.config.ApplicationProps;
import com.carbonwatch.carbonja.batch.tasklet.WorkloadTasklet;
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
public class WorkloadJobConfig extends AbstractModularJob {
    protected static final String JOB_NAME = "workload-job";

    @Autowired
    @Qualifier ("oneWebClient")
    WebClient oneWebClient;

    @Autowired
    @Qualifier ("twoWebClient")
    WebClient twoWebClient;

    @Autowired
    private ApplicationProps applicationProps;

    @Bean
    Job workloadJob(Step workloadStep, JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(workloadStep)
                .listener(reportListener())
                .build();
    }

    @Bean
    Step workloadStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("workload-step", jobRepository)
                .tasklet(workloadTasklet(), transactionManager)
                .build();
    }

    @Bean
    @JobScope
    WorkloadTasklet workloadTasklet (){
        return new WorkloadTasklet(
                new ItemReader(oneWebClient, twoWebClient, applicationProps)
        );
    }
}
