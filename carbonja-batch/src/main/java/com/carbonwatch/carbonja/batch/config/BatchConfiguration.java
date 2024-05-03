
package com.carbonwatch.carbonja.batch.config;

import com.carbonwatch.carbonja.domain.file.FileEntryVO;
import com.carbonwatch.carbonja.domain.file.FileRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BatchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    FileRepository fileRepository;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("hostJob")
    private Job hostJob;

    @Autowired
    @Qualifier("namespaceJob")
    private Job namespaceJob;

    @Autowired
    @Qualifier("processJob")
    private Job processJob;

    @Autowired
    @Qualifier("serviceJob")
    private Job serviceJob;

    @Autowired
    @Qualifier("workloadJob")
    private Job workloadJob;

    @PostConstruct
    public void executeJobs() throws InterruptedException {
        try {
            jobLauncher.run(hostJob, new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            logger.error("Error lors du lancement du job hostJob, ", e);
        }
        try {
            jobLauncher.run(namespaceJob, new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            logger.error("Error lors du lancement du job namespaceJob, ", e);
        }
        try {
            jobLauncher.run(processJob, new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            logger.error("Error lors du lancement du job processJob, ", e);
        }
        try {
            jobLauncher.run(serviceJob, new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            logger.error("Error lors du lancement du job serviceJob, ", e);
        }
        try {
            jobLauncher.run(workloadJob, new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            logger.error("Error lors du lancement du job workloadJob, ", e);
        }

        List<FileEntryVO> listObject = fileRepository.listObjects();

        Thread.sleep(3600000);

    }

}