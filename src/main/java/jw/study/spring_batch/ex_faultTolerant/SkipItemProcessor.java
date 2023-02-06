package jw.study.spring_batch.ex_faultTolerant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class SkipItemProcessor implements ItemProcessor<String, String> {
    private int cnt=0;
    @Override
    public String process(String item) throws Exception {

        if(item.equals("6") || item.equals("7")){
            throw new SkipException("processor exception: " + cnt);
        }
        else{
            log.info("processor: {}", item);
            return String.valueOf(Integer.valueOf(item) *-1);
        }
    }
}
