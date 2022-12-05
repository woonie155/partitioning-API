package jw.app.batch.job.api;

import jw.app.batch.listener.CustomJobListener;
import jw.app.batch.listener.CustomStepListener;
import jw.app.batch.tasklet.ApiEndTasklet;
import jw.app.batch.tasklet.ApiStartTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApiConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApiStartTasklet apiStartTasklet;
    private final ApiEndTasklet apiEndTasklet;
    private final Step jobStep;


    @Bean
    public Job apiJob(){
        return jobBuilderFactory.get("apiJob")
                .listener(new CustomJobListener())
                .start(apiStep1())
                .next(jobStep)
                .next(apiStep2())
                .build();
    }

    @Bean
    public Step apiStep1(){
        return stepBuilderFactory.get("apiStep1")
                .tasklet(apiStartTasklet)
                .listener(new CustomStepListener())
                .build();
    }
    @Bean
    public Step apiStep2(){
        return stepBuilderFactory.get("apiStep2")
                .tasklet(apiEndTasklet)
                .listener(new CustomStepListener())
                .build();
    }



}
