package jw.spring_batch.ex_chunk.itemStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

import java.util.List;

@Slf4j
public class CustomItemStreamWriter implements ItemStreamWriter<String> {

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        log.info("ItemStreamWriter: open 확인");
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        log.info("ItemStreamWriter: update 확인");
    }

    @Override
    public void close() throws ItemStreamException {
        log.info("ItemStreamWriter: close 확인");
    }

    @Override
    public void write(List<? extends String> items) throws Exception {
        items.forEach(item -> {
            log.info("ItemStreamWriter: write - {}", item);
        });
    }
}
