package net.atos.springrestclient.patient.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientResponse {

    private long id;

    private String name;

    private String lastName;

    private String pesel;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

}
