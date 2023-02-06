package jw.study.spring_batch.ex_chunk.itemReader.flatFileItem.delimited;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;

public class CustomDefaultLineMapper<T> implements LineMapper<T> {

    private LineTokenizer tokenizer;
    private FieldSetMapper<T> fieldSetMapper;


    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        return fieldSetMapper.mapFieldSet(tokenizer.tokenize(line));
    }

    public void setLineTokenizer(LineTokenizer lineTokenizer){
        this.tokenizer = lineTokenizer;
    }

    public void setFieldSetMapper(FieldSetMapper<T> fieldSetMapper){
        this.fieldSetMapper = fieldSetMapper;
    }
}
