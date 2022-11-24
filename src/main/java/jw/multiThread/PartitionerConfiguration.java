package jw.multiThread;

import jw.simpleTest.Customer7;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class PartitionerConfiguration {


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("batchJob1")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .listener(new StopWatchJobListener())
                .build();
    }

    public Step step1(){
        return stepBuilderFactory.get("PartitionStep1")
                .partitioner(slaveStep().getName(), partitioner())
                .step(slaveStep())
                .gridSize(4)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
    @Bean
    public Partitioner partitioner(){
        ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();
        columnRangePartitioner.setTable("customer7");
        columnRangePartitioner.setColumn("id"); //id 기반으로 파티셔닝
        columnRangePartitioner.setDataSource(dataSource);
        return columnRangePartitioner;
    }

    @Bean
    public Step slaveStep(){
        return stepBuilderFactory.get("slaveStep1")
                .<Customer7, Customer9>chunk(1000)
                .reader(reader(null, null))
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemStreamReader reader(
            @Value("#{stepExecutionContext['minValue']}") Long minValue,
            @Value("#{stepExecutionContext['maxValue']}") Long maxValue){
        return new JpaPagingItemReaderBuilder()
                .name("JpaPaging")
                .entityManagerFactory(entityManagerFactory)
                .currentItemCount(Math.toIntExact(minValue-1))
                .maxItemCount(Math.toIntExact(maxValue))
                .queryString("select c from Customer7 c")
                .build();
    }
    @Bean
    @StepScope
    public JdbcBatchItemWriter itemWriter() {
        return new JdbcBatchItemWriterBuilder<Customer9>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider())
                .sql("insert into customer9(id, first_name, last_name, birth_date) values (:id, :firstName, :lastName, :birthDate)")
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
