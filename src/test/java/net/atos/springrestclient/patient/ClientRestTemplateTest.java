package net.atos.springrestclient.patient;

import net.atos.springrestclient.patient.api.PatientRequest;
import org.junit.jupiter.api.Test;

import java.util.Date;

class ClientRestTemplateTest {

    ClientRestTemplate template = new ClientRestTemplate();

    @Test
    void listAllPatient() {
        template.listAllPatient();

    }

    @Test
    void listAllPatientDb() {
        template.listAllPatientDb();

    }

    @Test
    void listPatientDbById() {
        template.getOnePatientByIdDb(1);
        template.getOnePatientByIdDb(20);
    }

    @Test
    void createPatient() {

        PatientRequest patientRequest = PatientRequest.builder()
                                                      .name("Nowy")
                                                      .lastName("Użytkownik")
                                                      .pesel("2376527836578235")
                                                      .date(new Date())
                                                      .build();
        template.createPatientDb(patientRequest);
    }

    @Test
    void updatePatient() {

        PatientRequest patientRequest = PatientRequest.builder()
                                                      .name("Jan")
                                                      .lastName("Kowalski")
                                                      .build();
        template.updatePatientDb(11, patientRequest);
    }

    @Test
    void deletePatient() {
        template.deletePatientDb(11);
    }

}