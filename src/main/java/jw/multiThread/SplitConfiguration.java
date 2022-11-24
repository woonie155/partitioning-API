package jw.multiThread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SplitConfiguration {


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("batchJob1")
                .incrementer(new RunIdIncrementer())
                .start(flow1())
                    .split(taskExecutor()).add(flow2())
                    .end()
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Flow flow1(){
        TaskletStep step1 = stepBuilderFactory.get("step1")
                .tasklet(tasklet()).build();
        return new FlowBuilder<Flow>("flow1")
                .start(step1)
                .build();
    }
    @Bean
    public Flow flow2(){
        TaskletStep step2 = stepBuilderFactory.get("step2")
                .tasklet(tasklet()).build();
        TaskletStep step3 = stepBuilderFactory.get("step3")
                .tasklet(tasklet()).build();
        return new FlowBuilder<Flow>("flow2")
                .start(step2)
                .next(step3)
                .build();
    }


    @Bean
    public Tasklet tasklet(){
        return new CustomTasklet();
    }

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); //기본 동작시킬 스레드 개수(스레드 풀에 생성해 놓는다)
        executor.setMaxPoolSize(8); //총 스레드 생성 가능 개수(기본 스레드들이 대기나 기다리는 경우 동작된다)
        executor.setThreadNamePrefix("async-thread-");
        return executor;
    }
}
