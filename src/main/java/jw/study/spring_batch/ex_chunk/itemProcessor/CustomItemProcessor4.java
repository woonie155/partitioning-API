package jw.study.spring_batch.ex_chunk.itemProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class CustomItemProcessor4 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {
    @Override
    public ProcessorInfo process(ProcessorInfo item) throws Exception {
        log.info("custom 4");
        return item;
    }
}