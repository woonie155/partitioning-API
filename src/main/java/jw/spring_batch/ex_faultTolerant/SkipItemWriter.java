package jw.spring_batch.ex_faultTolerant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class SkipItemWriter implements ItemWriter<String> {
    private int cnt=0;
    @Override
    public void write(List<? extends String> items) throws Exception {
        for(String item : items){
            if(item.equals("-12")){
                throw new SkipException("writer exception: " + cnt);
            }
            else{
                log.info("writer: {}", item);
            }
        }
    }
}
