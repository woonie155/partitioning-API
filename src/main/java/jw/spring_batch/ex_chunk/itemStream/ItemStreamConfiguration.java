package jw.spring_batch.ex_chunk.itemStream;

import jw.spring_batch.ex_chunk.custom.CustomItemProcessor;
import jw.spring_batch.ex_chunk.custom.CustomItemReader;
import jw.spring_batch.ex_chunk.custom.CustomItemWriter;
import jw.spring_batch.ex_chunk.custom.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemStreamConfiguration {

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
                .<String, String>chunk(2)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    public CustomItemStreamReader itemReader(){
        List<String> items = new ArrayList<>(10);
        for(int i=0; i<10; i++){
            items.add(String.valueOf(i));
        }
        return new CustomItemStreamReader(items);
    }
    @Bean
    public ItemWriter<? super String> itemWriter(){
        return new CustomItemStreamWriter();
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
