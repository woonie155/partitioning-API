package jw.study.spring_batch.ex_chunk.custom;


import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

public class CustomItemReader implements ItemReader<Item> {

    private List<Item> item_list;

    public CustomItemReader(List<Item> items) {
        item_list = new ArrayList<>(items);
    }
    @Override
    public Item read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(!item_list.isEmpty()){
            return item_list.remove(0);
        }

        return null;
    }
}
