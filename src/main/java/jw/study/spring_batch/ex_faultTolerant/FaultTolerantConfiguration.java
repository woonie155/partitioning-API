package jw.study.spring_batch.ex_faultTolerant;

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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class FaultTolerantConfiguration {


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
    @JobScope
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(2)
                .reader(new ItemReader<String>() {
                    int i=0;
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        if(i==3){
                            throw new IllegalStateException("illegalException");
                        }
                        return i>20? null:String.valueOf(i);
                    }
                })
                .processor(itemProcessor())
                .writer(itemWriter())
                .faultTolerant()
//                .skipPolicy(customSkipPolicy())
                .skip(SkipException.class)
                .skipLimit(2)
                .build();
    }
    @Bean
    public ItemProcessor itemProcessor(){
        return new SkipItemProcessor();
    }
    @Bean
    public ItemWriter itemWriter(){
        return new SkipItemWriter();
    }
    @Bean
    public SkipPolicy customSkipPolicy(){ //커스텀 방식
        Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
        exceptionClass.put(SkipException.class, true);

        LimitCheckingItemSkipPolicy limitCheckingItemSkipPolicy = new LimitCheckingItemSkipPolicy(3, exceptionClass);
        return limitCheckingItemSkipPolicy;
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
