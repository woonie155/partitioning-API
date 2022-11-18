package jw.spring_batch.ex_chunk.custom;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class CustomItemWriter implements ItemWriter<Item> {
    @Override
    public void write(List<? extends Item> items) throws Exception{
        items.forEach(item -> {
            log.info("ItemWriter: {}", item);
        });
    }
}
