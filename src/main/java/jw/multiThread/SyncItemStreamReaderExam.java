package jw.multiThread;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;

public class SyncItemStreamReaderExam {

    @Bean
    public SynchronizedItemStreamReader reader(){
        JdbcCursorItemReader ex = new JdbcCursorItemReaderBuilder()
                .name("ex")
                .build();
        return new SynchronizedItemStreamReaderBuilder<>()
                .delegate(ex)
                .build();
    }
}
