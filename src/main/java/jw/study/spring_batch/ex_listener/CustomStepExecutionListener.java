package jw.study.spring_batch.ex_listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * 만약 빈으로 등록한다면, 다른 step 이어도 execution 공유할 수 있다.
 *
 */
public class CustomStepExecutionListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        stepExecution.getExecutionContext().put("name", "user1");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExitStatus exitStatus = stepExecution.getExitStatus();
        BatchStatus status = stepExecution.getStatus();
        return ExitStatus.COMPLETED;
    }
}
