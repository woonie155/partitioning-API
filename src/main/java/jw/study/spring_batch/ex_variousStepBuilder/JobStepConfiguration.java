package jw.study.spring_batch.ex_variousStepBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class JobStepConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job parentJob(){
        return jobBuilderFactory.get("parentJob1")
                .start(jobStep(null))
                .next(step2())
                .build();
    }

    @Bean
    public Step jobStep(JobLauncher jobLauncher){
        return stepBuilderFactory.get("jobStep1")
                .job(childJob())
                .launcher(jobLauncher)
                .parametersExtractor(jobParametersExtractor())
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        stepExecution.getExecutionContext().putString("name", "user2");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        return null;
                    }
                })
                .build();
    }

    private DefaultJobParametersExtractor jobParametersExtractor() {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"name"});
        return extractor;
    }

    @Bean
    public Job childJob(){
        return jobBuilderFactory.get("childJob1")
                .start(step1())
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
    public Step step2(){
        return stepBuilderFactory.get("Step2")
                .tasklet((a, b) ->{
                    log.info("Step2 확인");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
