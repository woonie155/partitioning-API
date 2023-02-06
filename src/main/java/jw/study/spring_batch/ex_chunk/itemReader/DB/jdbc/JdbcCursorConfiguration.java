package jw.study.spring_batch.ex_chunk.itemReader.DB.jdbc;

import jw.study.spring_batch.ex_chunk.itemReader.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class JdbcCursorConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job batchJob(){
        return jobBuilderFactory.get("batchJob1")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    @JobScope
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(3)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }
    @Bean
    public ItemReader<Customer> customItemReader(){
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("jdbcCursor")
                .fetchSize(3)
                .sql("select id, first_name, last_name, birth_date from customer where first_name like ? order by last_name, first_name")
                .beanRowMapper(Customer.class)
                .queryArguments("%")
                .dataSource(dataSource)
                .build();
    }


    @Bean
    public ItemWriter<Customer> customItemWriter(){
        return items ->{
            for(Customer u : items) log.info("write: {}", u);
            log.info("write: 청크단위 종료");
        };
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
