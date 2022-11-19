package jw.spring_batch.ex_chunk.itemReader.DB.jpa;

import jw.spring_batch.ex_chunk.itemReader.Customer;
import jw.spring_batch.ex_chunk.itemReader.Customer3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class JpaCursorConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

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
                .<Customer, Customer3>chunk(3)
                .reader(customItemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Customer> customItemReader(){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("firstname", "A%");

        return new JpaCursorItemReaderBuilder()
                .name("JpaCursor")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from Customer c where firstName like :firstname")
                .parameterValues(parameters)
                .build();
    }

    @Bean
    public ItemProcessor<? super Customer,? extends Customer3> customItemProcessor() {
        return new CustomItemProcessor();
    }


    @Bean
    public JpaItemWriter<Customer3> customItemWriter(){
        return new JpaItemWriterBuilder<Customer3>()
                .entityManagerFactory(entityManagerFactory)
                .build();
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
