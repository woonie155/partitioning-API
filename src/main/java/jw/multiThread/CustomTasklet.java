package jw.multiThread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class CustomTasklet implements Tasklet {

    private long sum; //-> 싱글톤 등록 및 병렬 수행하므로 동시성 문제 발생
    private ThreadLocal<Tmp> each_data = new ThreadLocal<>(); //thread-safe ->반면 스레드 풀에 의한 변수값 유지로, 재사용 주의
    private Object lock = new Object(); //sum 의미 처럼 사용하고 싶은 경우에 사용

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//        if(each_data.get()==null) each_data.set(new Tmp());
//        for(int i=0; i<10000000; i++){
//            each_data.get().plus();
//            sum++;
//        }
//        log.info("execute: {}, {}, {}, {}", chunkContext.getStepContext().getStepName(), Thread.currentThread().getName(), each_data.get().getSum(), sum);

        synchronized (lock){
            for(int i=0; i<10000000; i++){
                sum++;
            }
            log.info("execute: {}, {}, {}", chunkContext.getStepContext().getStepName(), Thread.currentThread().getName(), sum);
        }

        return RepeatStatus.FINISHED;
    }
}
