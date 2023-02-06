package jw.study.spring_batch.ex_chunk.custom;


import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Item, Item> {


    @Override
    public Item process(Item item) throws Exception {
        item.setName(item.getName().toUpperCase());
        return item;
    }
}
