package jw.study.spring_batch.ex_jobRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobRepositoryListener implements JobExecutionListener {

    @Autowired private JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "20221112")
                .toJobParameters();

        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
        if(lastJobExecution != null){
            for(StepExecution execution: lastJobExecution.getStepExecutions()){
                Long jobExecutionId = execution.getJobExecutionId();
                BatchStatus status = execution.getStatus();
                ExitStatus exitStatus = execution.getExitStatus();
                String stepName = execution.getStepName();
                log.info("JobRepositoryListener >>>");
                log.info("jobExecutionId: {}", jobExecutionId);
                log.info("status: {}", status);
                log.info("exitStatus: {}", exitStatus);
                log.info("stepName: {}", stepName);
            }
        }
    }
}
