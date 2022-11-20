package jw.spring_batch.ex_faultTolerant.retry;

import jw.spring_batch.ex_faultTolerant.SkipException;
import jw.spring_batch.ex_faultTolerant.SkipItemProcessor;
import jw.spring_batch.ex_faultTolerant.SkipItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RetryConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob(){
        return jobBuilderFactory.get("batchJob1")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(items -> items.forEach(it->System.out.println(it)))
                .faultTolerant()
                .skip(RetryException.class)
                .skipLimit(2)
//                .retryPolicy(retryPolicy())
                .retry(RetryException.class)
                .retryLimit(2)
                .build();
    }
    @Bean
    public ListItemReader reader(){
        List<String> items = new ArrayList<>();
        for(int i=0; i<30; i++){
            items.add(String.valueOf(i));
        }
        log.info("reader 확인");
        return new ListItemReader<>(items);
    }
    @Bean
    public ItemProcessor processor(){
        return new RetryItemProcessor();
    }


    @Bean
    public RetryPolicy retryPolicy(){
        Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
        exceptionClass.put(RetryException.class, true);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(2, exceptionClass);
        return simpleRetryPolicy;
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((a, b)->{
                    log.info("step2 확인");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
