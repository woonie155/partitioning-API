package jw.spring_batch.ex_chunk.itemReader.adapter;

import jw.spring_batch.ex_chunk.itemReader.Customer2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemReaderAdapterConfiguration {


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
                .<String, String>chunk(4)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }
    @Bean
    public ItemReader<String> customItemReader() {
        ItemReaderAdapter<String> reader = new ItemReaderAdapter<>();
        reader.setTargetObject(customService());
        reader.setTargetMethod("customRead");
        return reader;
    }
    @Bean
    public CustomService customService(){
        return new CustomService();
    }

    @Bean
    public ItemWriter<String> customItemWriter(){
        ItemWriterAdapter<String> writer = new ItemWriterAdapter<>();
        writer.setTargetObject(customService());
        writer.setTargetMethod("customWrite");
        return writer;
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
