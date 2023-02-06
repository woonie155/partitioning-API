package jw.study.spring_batch.ex_variousStepBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class TaskletStepConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob(){
        return jobBuilderFactory.get("batchJob1")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1(){ //TaskletStepBuilder
        return stepBuilderFactory.get("Step1")
                .tasklet((a, b) ->{
                    log.info("Step1 확인");
                    return RepeatStatus.FINISHED;
                })
                .allowStartIfComplete(true)
                .startLimit(4)
                .build();
    }

    @Bean
    public Step step2(){ //TaskletStepBuilder
        return stepBuilderFactory.get("Step2")
                .tasklet((a, b) ->{
                    log.info("Step1 확인");
                    throw new RuntimeException("d");
//                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
