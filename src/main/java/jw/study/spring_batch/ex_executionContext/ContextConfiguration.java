package jw.study.spring_batch.ex_executionContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class ContextConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob(){
        return jobBuilderFactory.get("helloJob")
                .start(step1())
                .next(step2())
                .next(step3())
                .next(step4())
                .build();
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("helloStep1")
                .tasklet(new CustomTasklet())
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("helloStep2")
                .tasklet(new CustomTasklet2())
                .build();
    }

    @Bean
    public Step step3(){
        return stepBuilderFactory.get("helloStep3")
                .tasklet(new CustomTasklet3())
                .build();
    }
    @Bean
    public Step step4(){
        return stepBuilderFactory.get("helloStep4")
                .tasklet(new CustomTasklet4())
                .build();
    }
}
