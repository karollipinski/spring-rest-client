package net.atos.springrestclient.patient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClientWebClientTest {

    @Autowired
    ClientWebClient webClient;

    @Test
    void listAllPatient() {

        webClient.listAllPatient();

    }
}