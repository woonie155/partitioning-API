package jw.study.spring_batch.ex_listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CustomJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long start_time = jobExecution.getStartTime().getTime();
        long end_time = jobExecution.getEndTime().getTime();
    }
}
