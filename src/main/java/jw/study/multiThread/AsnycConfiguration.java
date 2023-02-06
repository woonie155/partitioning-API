package jw.study.multiThread;

import jw.study.simpleTest.Customer7;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class AsnycConfiguration {

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

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<Customer7, Customer9>chunk(100)
                .reader(reader())
                .processor(asyncItemProcessor())
                .writer(asyncItemWriter())
                .build();
    }
    
    @Bean
    public JpaPagingItemReader reader(){
        return new JpaPagingItemReaderBuilder()
                .name("JpaPaging")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString("select c from Customer7 c")
                .build();
    }

    @Bean
    public AsyncItemProcessor asyncItemProcessor() throws Exception {
        AsyncItemProcessor<Customer7, Customer9> asyncItemProcessor = new AsyncItemProcessor();
        asyncItemProcessor.setDelegate(customItemProcessor());
        asyncItemProcessor.setTaskExecutor(taskExecutor());
        return asyncItemProcessor;
    }
    @Bean
    public ItemProcessor customItemProcessor(){
        return new ItemProcessor<Customer7, Customer9>() {
            @Override
            public Customer9 process(Customer7 item) throws Exception {
//                Thread.sleep(100);
                return new Customer9(item.getId(),
                        item.getFirstName().toUpperCase(),
                        item.getLastName().toUpperCase(),
                        item.getBirthDate());
            }
        };
    }
    
    @Bean
    public AsyncItemWriter asyncItemWriter() throws Exception {
        AsyncItemWriter asyncItemWriter = new AsyncItemWriter();
        asyncItemWriter.setDelegate(customItemWriter());
        return asyncItemWriter;
    }

    @Bean
    public JdbcBatchItemWriter customItemWriter() {
        return new JdbcBatchItemWriterBuilder<Customer9>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider())
                .sql("insert into customer9(id, first_name, last_name, birth_date) values (:id, :firstName, :lastName, :birthDate)")
                .build();
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
