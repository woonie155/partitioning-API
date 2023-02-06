package jw.study.spring_batch.ex_scope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class ScopeConfiguration {


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob(){
        return jobBuilderFactory.get("batchJob1")
                .incrementer(new RunIdIncrementer())
                .start(step1(null))
                .next(step2())
                .listener(new CustomJobListener())
                .build();
    }


    @Bean
    @JobScope
    public Step step1(@Value("#{jobParameters['message']}") String message) {
        log.info("message 확인 : {}", message);
        return stepBuilderFactory.get("step1")
                .tasklet(tasklet1(null))
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(tasklet2(null))
                .listener(new CustomStepListener())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet tasklet1(@Value("#{jobExecutionContext['name']}") String name){
        return (a, b)->{
            log.info("tasklet1 확인 : {}", name);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope
    public Tasklet tasklet2(@Value("#{stepExecutionContext['name2']}") String name2){
        return (a, b)->{
            log.info("tasklet2 확인 :{}", name2);
            return RepeatStatus.FINISHED;
        };
    }
}
