package jw.spring_batch.ex_chunk.itemReader;

import lombok.Data;

@Data
public class Customer {

    private int id;
    private String firstName;
    private String lastName;
    private String birthdate;
}
