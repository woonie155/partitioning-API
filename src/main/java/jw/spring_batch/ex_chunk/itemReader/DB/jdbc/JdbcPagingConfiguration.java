package jw.spring_batch.ex_chunk.itemReader.DB.jdbc;

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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class JdbcPagingConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job batchJob() throws Exception {
        return jobBuilderFactory.get("batchJob1")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    @JobScope
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer3>chunk(10)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }
    @Bean
    public JdbcPagingItemReader<Customer> customItemReader() throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("firstname", "A%");

        return new JdbcPagingItemReaderBuilder<Customer>()
                .name("JdbcPaging")
                .pageSize(10)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Customer.class))
                .queryProvider(createQueryProvider())
                .parameterValues(parameters)
                .build();
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean sqlQB = new SqlPagingQueryProviderFactoryBean();
        sqlQB.setDataSource(dataSource);
        sqlQB.setSelectClause("id, first_name, last_name, birth_date");
        sqlQB.setFromClause("from customer");
        sqlQB.setWhereClause("where first_name like :firstname");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);

        sqlQB.setSortKeys(sortKeys);
        return sqlQB.getObject();
    }


    @Bean
    public ItemWriter customItemWriter(){
        return new JdbcBatchItemWriterBuilder()
                .dataSource(dataSource)
                .sql("insert into customer3 values (:id, :firstName, :lastName, :birthDate)")
                .beanMapped()
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
