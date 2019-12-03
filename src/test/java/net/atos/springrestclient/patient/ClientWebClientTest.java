package net.atos.springrestclient.patient;

import lombok.extern.slf4j.Slf4j;
import net.atos.springrestclient.patient.api.PatientRequest;
import net.atos.springrestclient.patient.api.PatientResponse;
import net.atos.springrestclient.patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@Slf4j
@SpringBootTest
class ClientWebClientTest {

    @Autowired
    ClientWebClient webClient;

    @Autowired
    PatientService patientService;

    @Test
    void listAllPatient() {
        webClient.listAllPatient();
    }

    @Test
    void listAllPatient2Example() {
        webClient.listAllPatient2Example();
    }

    @Test
    void getPatientWithNameAdam() {
        patientService.getPatientWithNameAdam()
                      .block();
    }

    @Test
    void createPatient() {

        PatientRequest patientRequest = PatientRequest.builder()
                                                      .name("Adam")
                                                      .lastName("Mickiewicz")
                                                      .pesel("78782783428732")
                                                      .date(new Date())
                                                      .build();

        PatientResponse patientResponse = patientService.createPatient(patientRequest)
                                                        .block();

        log.info("Nowy pacjent {}", patientResponse);
    }

}