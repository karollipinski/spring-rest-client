package net.atos.springrestclient.patient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientRestTemplateTest {

    @Test
    void listAllPatient() {

        ClientRestTemplate template = new ClientRestTemplate();
        template.listAllPatient();

    }
}