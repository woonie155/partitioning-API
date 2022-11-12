package jw.spring_batch.ex_variousStepBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class Various_StepConfiguration {


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob(){
        return jobBuilderFactory.get("batchJob1")
                .start(step1())
                .next(step2())
                .next(step3())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step1(){ //TaskletStepBuilder
        return stepBuilderFactory.get("Step1")
                .tasklet((a, b) ->{
                    log.info("Step1 확인");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step2(){ //SimpleStepBuilder
        return stepBuilderFactory.get("Step2")
                .<String, String>chunk(3)
                .reader(new ItemReader<String>() {
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        return null;
                    }
                })
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        return null;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {

                    }
                })
                .build();
    }
    @Bean
    public Step step3(){ //PartitionStepBuilder
        return stepBuilderFactory.get("Step3")
                .partitioner(step1())
                .gridSize(2)
                .build();
    }
    @Bean
    public Step step4(){ //JobStepBuilder
        return stepBuilderFactory.get("Step4")
                .job(job())
                .build();
    }
    @Bean
    public Step step5(){ //FlowStepBuilder
        return stepBuilderFactory.get("Step5")
                .flow(flow())
                .build();
    }
    @Bean
    public Job job(){
        return jobBuilderFactory.get("job")
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }
    @Bean
    public Flow flow(){
        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("flow");
        flowFlowBuilder.start(step2()).end();
        return flowFlowBuilder.build();
    }

}
