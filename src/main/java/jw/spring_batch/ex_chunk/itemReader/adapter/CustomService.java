package jw.spring_batch.ex_chunk.itemReader.adapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomService<T> {

    private int cnt = 0;

    public T customRead(){
        if(cnt == 50) return null;
        return (T)("item" + cnt++);
    }

    public void customWrite(T item){
        log.info("customWrite: {}", item);
    }

}
