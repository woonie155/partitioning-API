package jw.study.spring_batch.ex_jobRepository;

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
public class RepositoryConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobRepositoryListener jobRepositoryListener;

    @Bean
    public Job repositoryJob(){
        return jobBuilderFactory.get("RepositoryJob")
                .start(step1())
                .next(step2())
                .listener(jobRepositoryListener)
                .build();
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("RepositoryStep1")
                .tasklet((a, b) ->{
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("RepositoryStep2")
                .tasklet((a, b) -> null)
                .build();
    }
}
