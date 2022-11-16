package jw.spring_batch.ex_flowjob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FlowJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob(){
        return jobBuilderFactory.get("batchJob1")
                .start(step1())
                    .on("FAILED")
                    .to(step2())
                    .on("PASS")
                    .stop()
                .from(step1())
                    .on("*")
                    .to(step3())
                    .next(step4())
                .from(step2())
                    .on("*")
                    .to(step5())
                .end()
                .build();
    }

    @Bean
    public Job batchJob2() {
        return jobBuilderFactory.get("batchJob2")
                .start(flow1())
                .next(step3())
                .next(flow2())
                .next(step6())
                .end()
                .build();
    }

    @Bean
    public Flow flow1(){
        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("flow1");
        flowFlowBuilder.start(step1())
                .next(step2())
                .end();
        return flowFlowBuilder.build();
    }
    @Bean
    public Flow flow2(){
        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("flow2");
        flowFlowBuilder.start(step4())
                .next(step5())
                .end();
        return flowFlowBuilder.build();
    }



    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((a, b) -> {
                    log.info("Step1 확인");
//                    throw new RuntimeException("step1 실패");
                    a.getStepExecution().setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((a, b) -> {
                    log.info("Step2 확인");
                    return RepeatStatus.FINISHED;
                })
                .listener(new PassCheckingListener())
                .build();
    }
    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((a, b) -> {
                    log.info("Step3 확인");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .tasklet((a, b) -> {
                    log.info("Step4 확인");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step5() {
        return stepBuilderFactory.get("step5")
                .tasklet((a, b) -> {
                    log.info("Step5 확인");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step6() {
        return stepBuilderFactory.get("step6")
                .tasklet((a, b) -> {
                    log.info("Step6 확인");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
