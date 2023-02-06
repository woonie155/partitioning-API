package jw.study.spring_batch.ex_chunk.itemReader.jsonItem;

import jw.study.spring_batch.ex_chunk.itemReader.flatFileItem.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;


@Slf4j
@RequiredArgsConstructor
//@Configuration
public class JsonConfiguration {


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
                .<User, User>chunk(3)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }
    @Bean
    public ItemReader<User> customItemReader(){
        return new JsonItemReaderBuilder<User>()
                .name("json1")
                .resource(new ClassPathResource("/user4.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(User.class))
                .build();
    }


    @Bean
    public ItemWriter customItemWriter(){
        return new JsonFileItemWriterBuilder()
                .name("jsonWriter1")
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(new FileSystemResource("C:\\Users\\nolan\\OneDrive\\바탕 화면\\spring_batch\\src\\main\\resources\\user5.json"))
                .build();
        //append(boolean): 초기화해 쓸 것인지, 뒤에 붙여 쓸 것인지 결정
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
