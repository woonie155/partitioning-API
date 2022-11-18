package jw.spring_batch.ex_chunk.itemReader.flatFileItem.delimited;

import jw.spring_batch.ex_chunk.itemReader.flatFileItem.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FlatFileDelimitedConfiguration {


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

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
                .<String, String>chunk(4)
                .reader(delimitedItemReader())
                .writer(new ItemWriter() {
                    @Override
                    public void write(List items) throws Exception {
                        log.info("writer: {}", items);
                    }
                })
                .build();
    }

//    @Bean
//    public ItemReader delimitedItemReader(){
//        FlatFileItemReader<User> itemReader = new FlatFileItemReader<>();
//        itemReader.setResource(new ClassPathResource("/user.csv"));
//
//        CustomDefaultLineMapper<User> lineMapper = new CustomDefaultLineMapper<>();
//        lineMapper.setLineTokenizer(new DelimitedLineTokenizer()); //스프링 배치 제공
//        lineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
//        itemReader.setLineMapper(lineMapper);
//        itemReader.setLinesToSkip(1);
//
//        return itemReader;
//    }
//    @Bean
//    public ItemReader delimitedItemReader(){
//        return new FlatFileItemReaderBuilder<User>()
//                .name("flatFile")
//                .resource(new ClassPathResource("/user.csv"))
//                .fieldSetMapper(new CustomerFieldSetMapper())
//                .linesToSkip(1)
//                .delimited().delimiter(",")
//                .names("name", "age", "year")
//                .build();
//    }
        @Bean
        public ItemReader delimitedItemReader(){
            return new FlatFileItemReaderBuilder<User>()
                    .name("flatFile1") //executionContext 저장용(키값)
                    .resource(new ClassPathResource("/user.csv"))
                    .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                    .targetType(User.class)
                    .linesToSkip(1)
                    .delimited().delimiter(",")
                    .names("name", "age", "year")
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
