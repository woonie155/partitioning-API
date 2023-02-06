package jw.study.spring_batch.ex_chunk.itemReader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;
    private String location;

    @OneToOne
    @JoinColumn(name = "customer2_id")
    private Customer2 customer2;
}
