package jw.spring_batch.ex_listener;

import org.springframework.batch.core.ItemProcessListener;

public class CustomProcessListener implements ItemProcessListener<Integer, String> {

    @Override
    public void beforeProcess(Integer item) {

    }

    @Override
    public void afterProcess(Integer item, String result) {

    }

    @Override
    public void onProcessError(Integer item, Exception e) {

    }
}
