package jw.spring_batch.ex_chunk.itemReader.flatFileItem.delimited;

import jw.spring_batch.ex_chunk.itemReader.flatFileItem.User;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomerFieldSetMapper implements FieldSetMapper<User> {
    @Override
    public User mapFieldSet(FieldSet fieldSet) throws BindException {
        if(fieldSet==null){
            return null;
        }

        User user = new User();
        user.setName(fieldSet.readString("name"));
        user.setAge(fieldSet.readInt("age"));
        user.setYear(fieldSet.readString("year"));
        return user;
    }
}
