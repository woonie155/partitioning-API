package jw.application.batch.job.api;

import jw.application.batch.listener.CustomJobListener;
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
public class ApiJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final Step jobStep;

    @Bean
    public Job apiJob(){
        return jobBuilderFactory.get("apiJob")
                .listener(new CustomJobListener())
                .start(jobStep)
                .build();
    }
}
