package jw.study.spring_batch.ex_validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class Validator_JobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob(){
        return jobBuilderFactory.get("batchJob1")
                .start(step1())
                .next(step2())
                .next(step3())
                .validator(new DefaultJobParametersValidator(new String[]{"name"}, new String[]{}))
//                .validator(new CustomJobParametersValidator())
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
    @Bean
    public Step step3() {
        return stepBuilderFactory.get("Step3")
                .tasklet((a, b) ->{
                    log.info("Step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
