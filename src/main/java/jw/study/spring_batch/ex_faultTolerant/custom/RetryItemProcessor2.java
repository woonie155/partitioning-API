package jw.study.spring_batch.ex_faultTolerant.custom;

import jw.study.spring_batch.ex_faultTolerant.retry.RetryException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

public class RetryItemProcessor2 implements ItemProcessor<String, Customer5> {

    @Autowired
    private RetryTemplate retryTemplate;
    private int cnt = 0;


    @Override
    public Customer5 process(String item) throws Exception {

        Customer5 customer5 = retryTemplate.execute(new RetryCallback<Customer5, RuntimeException>() {
            @Override
            public Customer5 doWithRetry(RetryContext context) throws RuntimeException {
                if(item.equals("1") || item.equals("2")){
                    cnt++;
                    throw new RetryException("RetryCallback error cnt: "+ cnt);
                }

                return new Customer5(item);
            }
        }, new RecoveryCallback<Customer5>() {
            @Override
            public Customer5 recover(RetryContext context) throws Exception {
                return new Customer5(item);
            }
        });
        //DefaultRetryState 파라미터 적용안할 시, RetryCallback 으로인한 재시작처리 건너뛴다.
        // -> count 는 증가하므로 recover 동작할 순 있다.

        return customer5;
    }
}
