package com.carbonwatch.carbonja.batch.job;

import com.carbonwatch.carbonja.batch.config.ApplicationProps;
import com.carbonwatch.carbonja.batch.common.ItemReader;
import com.carbonwatch.carbonja.batch.tasklet.ProcessTasklet;
import com.carbonwatch.carbonja.batch.listener.AbstractModularJob;
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
public class ProcessJobConfig extends AbstractModularJob {

    protected static final String JOB_NAME = "process-job";

    @Autowired
    @Qualifier ("oneWebClient")
    WebClient oneWebClient;

    @Autowired
    @Qualifier ("twoWebClient")
    WebClient twoWebClient;

    @Autowired
    private ApplicationProps applicationProps;

    @Bean
    Job processJob(Step processStep, JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processStep)
                .listener(reportListener())
                .build();
    }

    @Bean
    Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("process-step", jobRepository)
                .tasklet(processTasklet(), transactionManager)
                .build();
    }

    @Bean
    @JobScope
    ProcessTasklet processTasklet (){
        return new ProcessTasklet(
                new ItemReader(oneWebClient, twoWebClient, applicationProps)
        );
    }
}
