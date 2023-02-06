package jw.study.simpleTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CusDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;

    public String getFirst_name() {
        return firstName;
    }
    public String getLast_name() {
        return lastName;
    }
    public Date getBirth_date() {
        return birthDate;
    }
}
