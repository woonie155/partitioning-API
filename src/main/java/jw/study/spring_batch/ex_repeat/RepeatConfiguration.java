package jw.study.spring_batch.ex_repeat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;



@Slf4j
@RequiredArgsConstructor
//@Configuration
public class RepeatConfiguration {

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
                        return i>3? null:"item"+i;
                    }
                })
                .processor(new ItemProcessor<String, String>() {
                    RepeatTemplate repeatTemplate =new RepeatTemplate();
                    @Override
                    public String process(String item) throws Exception {
//                        repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(3));
//                        repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(1000));
                        CompositeCompletionPolicy compositeCompletionPolicy = new CompositeCompletionPolicy();
                        CompletionPolicy[] completionPolicies
                                = new CompletionPolicy[]{new SimpleCompletionPolicy(3), new TimeoutTerminationPolicy(1000)};
                        compositeCompletionPolicy.setPolicies(completionPolicies);
                        repeatTemplate.setCompletionPolicy(compositeCompletionPolicy);
                        repeatTemplate.setExceptionHandler(simpleLimitExceptionHandler()); //예외 3번 허용
                        repeatTemplate.iterate(new RepeatCallback() {
                            @Override
                            public RepeatStatus doInIteration(RepeatContext context) throws Exception {
                                log.info("repeatTemplate{} 확인{}", item, context.getStartedCount());
                                return RepeatStatus.CONTINUABLE;
                            }
                        });
                        return item;
                    }
                })
                .writer(items -> log.info("w: {}", items))
                .build();
    }

    @Bean
    public ExceptionHandler simpleLimitExceptionHandler(){
        return new SimpleLimitExceptionHandler(3);
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
