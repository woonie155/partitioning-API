package jw.spring_batch.ex_flowjob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobExecutionDeciderConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob(){
        return jobBuilderFactory.get("batchJob1")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(decider())
                    .from(decider()).on("ODD").to(oddStep())
                    .from(decider()).on("EVEN").to(evenStep())
                .end()
                .build();
    }

    @Bean
    public JobExecutionDecider decider(){
        return new CustomDecider();
    }


    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((a, b) -> {
                    log.info("Step1 확인");
//                    throw new RuntimeException("step1 실패");
//                    a.getStepExecution().setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((a, b) -> {
                    log.info("Step2 확인");
                    return RepeatStatus.FINISHED;
                })
                .listener(new PassCheckingListener())
                .build();
    }

    @Bean
    public Step evenStep(){
        return stepBuilderFactory.get("evenStep")
                .tasklet((a, b)->{
                    log.info("evenStep 확인");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step oddStep(){
        return stepBuilderFactory.get("oddStep")
                .tasklet((a, b)->{
                    log.info("oddStep 확인");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
