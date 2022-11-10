package jw.spring_batch.ex_executionContext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class CustomTasklet3 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        Object name = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("name");
        if(name == null){
            chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("name", "user1"); //두번째는 name 존재
            throw new RuntimeException("T3 -> name 없었던 경우");
        }
        return RepeatStatus.FINISHED;
    }
}
