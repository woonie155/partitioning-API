package jw.simpleTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer8 {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
}
