package jw.study.multiThread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer9 {

    @Id @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;

    @Temporal(TemporalType.DATE)
    private Date birthDate;
}
