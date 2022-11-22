package jw.spring_batch;

import jw.simpleTest.SimpleJobConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes={SimpleJobConfiguration.class, TestBatchConfig.class})
public class SimpleJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @After
    public void clear() throws Exception{
//        jdbcTemplate.execute("delete from customer8");
    }

    @Test
    public void simpleJob_test() throws Exception {
        //given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user1")
                .addLong("date", new Date().getTime())
                .toJobParameters();


        //when
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        JobExecution step1 = jobLauncherTestUtils.launchStep("step1");
        StepExecution stepExecution = (StepExecution) ((List) step1.getStepExecutions()).get(0);

        //then
//        Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
//        Assert.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);

        Assert.assertEquals(stepExecution.getCommitCount(), 2);
        Assert.assertEquals(stepExecution.getReadCount(), 100);
        Assert.assertEquals(stepExecution.getWriteCount(), 100);
    }

}
