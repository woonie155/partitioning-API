package jw.study.spring_batch.ex_increment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class Increment_JobConfiguration {


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob(){
        return jobBuilderFactory.get("batchJob1")
                .start(step1())
                .next(step2())
//                .incrementer(new RunIdIncrementer())
                .incrementer(new CustomJobParametersIncrementer())
                .build();
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("Step1")
                .tasklet((a, b) ->{
                    log.info("Step1 확인");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step2(){
        return stepBuilderFactory.get("Step2")
                .tasklet((a, b) ->{
                    log.info("Step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
