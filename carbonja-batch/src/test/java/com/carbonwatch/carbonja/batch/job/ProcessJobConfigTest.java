//package com.carbonwatch.carbonja.batch.job;
//
//import com.carbonwatch.carbonja.batch.BatchTestConfiguration;
//import com.carbonwatch.carbonja.batch.GreenecoBatchApp;
//import org.junit.jupiter.api.Test;
//import org.springframework.batch.core.BatchStatus;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.test.JobLauncherTestUtils;
//import org.springframework.batch.test.context.SpringBatchTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ActiveProfiles({ "test" })
//@SpringBatchTest
//@SpringBootTest(classes = { GreenecoBatchApp.class, ServiceJobConfig.class, BatchTestConfiguration.class }, properties = {
//        "spring.batch.job.enabled=false", "spring.cloud.vault.enabled=false", "spring.batch.job.names=service-job" })
//public class ServiceJobConfigTest {
//
//    @Autowired
//    private JobLauncherTestUtils testUtils;
//
//    @Test
//    void should_return_status_completed_when_launch_serviceJob() throws Exception {
//
//        final JobParametersBuilder jobParameterBuilder = new JobParametersBuilder();
//
//        // run job
//        final JobExecution jobExecution = testUtils.launchJob(jobParameterBuilder.toJobParameters());
//
//        // - job status
//        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
//    }
//
//}
