package jw.study.spring_batch.ex_executionContext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class CustomTasklet2 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();

        log.info("tasklet2 jobName->{}", jobExecutionContext.get("jobName")); //공유 가능하다.
        log.info("tasklet2 stepName->{}", stepExecutionContext.get("stepName")); //공유 못한다. (null)

        return RepeatStatus.FINISHED;
    }
}
