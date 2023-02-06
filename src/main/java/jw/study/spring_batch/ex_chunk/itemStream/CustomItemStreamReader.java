package jw.study.spring_batch.ex_chunk.itemStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;

import java.util.List;

@Slf4j
public class CustomItemStreamReader implements ItemStreamReader<String> {

    private final List<String> items;
    private int index = -1;
    private boolean restart = false;

    public CustomItemStreamReader(List<String> items) {
        this.items = items;
        this.index = 0;
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        String item = null;

        if(this.index < this.items.size()){
            item = this.items.get(index);
            index++;
        }

        if(this.index == 6 && !restart){
            throw new RuntimeException("재시작 필요");
        }

        return item;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        log.info("ItemStreamReader: open 확인");
        if(executionContext.containsKey("index")){
            index = executionContext.getInt("index");
            this.restart = true;
        }else{
            index = 0;
            executionContext.put("index", index);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.put("index", index);
    }

    @Override
    public void close() throws ItemStreamException {
        log.info("ItemStreamReader: close 확인");
    }
}
