package jw.study.simpleTest;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer7 {

    @Id @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    @Temporal(TemporalType.DATE)
    private Date birthDate;
}