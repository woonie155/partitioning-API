package jw.study.spring_batch.ex_faultTolerant.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class RetryItemProcessor implements ItemProcessor<String, String> {
    private int cnt=0;
    @Override
    public String process(String item) throws Exception {
        log.info("processor {}:: cnt -{}", item,cnt);
        if(item.equals("2") || item.equals("3")){
            cnt++;
            throw new RetryException("RetryException: "+ cnt);
        }
        return item;
    }
}
