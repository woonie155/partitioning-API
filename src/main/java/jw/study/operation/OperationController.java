package jw.study.operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.Set;


@Slf4j
@RestController
@RequiredArgsConstructor
public class OperationController {

    private final JobRegistry jobRegistry;
    private final JobExplorer jobExplorer;
    private final JobOperator jobOperator;

    @PostMapping("/batch/start")
    public String start(@RequestBody JobInfo jobInfo) throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {

        jobRegistry.getJobNames().iterator();
        for(Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext();){
            SimpleJob simpleJob = (SimpleJob)jobRegistry.getJob(iterator.next());

            log.info("@: {}", simpleJob);
            jobOperator.start(simpleJob.getName(), "id="+jobInfo.getId()); //실행
        }

        return "return: start method";
    }

    @PostMapping("/batch/stop")
    public String stop() throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException {
        for(Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext();) {
            SimpleJob simpleJob = (SimpleJob) jobRegistry.getJob(iterator.next());

            Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(simpleJob.getName());
            JobExecution jobExecution = runningJobExecutions.iterator().next(); //하나뿐이니 세부선택은 생략
            jobOperator.stop(jobExecution.getId()); //실행중인 job 중단(해당 step 완료 후 중단)
        }

        return "return: stop method ";
    }

    @PostMapping("/batch/restart")
    public String restart() throws NoSuchJobException, NoSuchJobExecutionException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException {
        for(Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext();) {
            SimpleJob simpleJob = (SimpleJob) jobRegistry.getJob(iterator.next());

            JobInstance lastJobInstance = jobExplorer.getLastJobInstance(simpleJob.getName());
            JobExecution lastJobExecution = jobExplorer.getLastJobExecution(lastJobInstance);
            jobOperator.restart(lastJobExecution.getId());
            //마지막 instance 그대로 사용해 재실행한다. (execution 만 추가생성)
            //해당 job 실패한 step 부터가 아닌, 처음부터 재시작한다.
        }

        return "return: restart method ";
    }
}
