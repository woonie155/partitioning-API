package jw.spring_batch.ex_chunk.itemReader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer3 {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String birthDate;
}
