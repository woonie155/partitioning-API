package jw.spring_batch.ex_chunk.itemReader.xmlItem;

import jw.spring_batch.ex_chunk.itemReader.flatFileItem.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class XMLConfiguration {

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
    public StaxEventItemReader<User> customItemReader(){
        return new StaxEventItemReaderBuilder<User>()
                .name("staxXML")
                .resource(new ClassPathResource("user3.xml"))
                .addFragmentRootElements("user")
                .unmarshaller(itemUnMarshaller())
                .build();
    }

    @Bean
    public XStreamMarshaller itemUnMarshaller(){
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("user", User.class);
        aliases.put("name", String.class);
        aliases.put("age", Integer.class);
        aliases.put("year", String.class);

        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setAliases(aliases);
        return xStreamMarshaller;
    }

    @Bean
    public ItemWriter<User> customItemWriter(){
        return items ->{
            for(User u : items) log.info("write: {}", u);
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
