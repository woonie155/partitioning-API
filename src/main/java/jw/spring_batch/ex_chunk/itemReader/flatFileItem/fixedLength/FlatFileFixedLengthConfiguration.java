package jw.spring_batch.ex_chunk.itemReader.flatFileItem.fixedLength;

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
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FlatFileFixedLengthConfiguration {

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
                .reader(fixedLengthItemReader())
                .writer(new ItemWriter() {
                    @Override
                    public void write(List items) throws Exception {
                        log.info("writer: {}", items);
                    }
                })
                .build();
    }

    @Bean
    public ItemReader fixedLengthItemReader(){
        return new FlatFileItemReaderBuilder<User>()
                .name("flatFile2")
                .resource(new FileSystemResource("C:\\Users\\nolan\\OneDrive\\바탕 화면\\spring_batch\\src\\main\\resources\\user2.txt"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .targetType(User.class)
                .linesToSkip(1)
                .fixedLength()
                .addColumns(new Range(1,5))
                .addColumns(new Range(6,9))
                .addColumns(new Range(10,11))
                .names("name","year","age")
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
