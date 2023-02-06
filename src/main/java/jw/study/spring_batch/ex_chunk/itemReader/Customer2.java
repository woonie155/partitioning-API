package jw.study.spring_batch.ex_chunk.itemReader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer2 {

    @Id @GeneratedValue
    private Long id;
    private String userName;
    private int age;

    @OneToOne(mappedBy = "customer2")
    private Address address;
}
