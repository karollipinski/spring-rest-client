package net.atos.springrestclient.patient.api;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
public class PatientRequest {

    private String name;

    private String lastName;

    private String pesel;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

}
