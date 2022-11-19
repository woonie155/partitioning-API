package jw.spring_batch.ex_chunk.itemReader.adapter;


public class CustomService<T> {

    private int cnt = 0;

    public T customRead(){
        if(cnt == 50) return null;
        return (T)("item" + cnt++);
    }

}
