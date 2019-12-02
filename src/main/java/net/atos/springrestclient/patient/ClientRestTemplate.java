package net.atos.springrestclient.patient;

import lombok.extern.slf4j.Slf4j;
import net.atos.springrestclient.patient.api.PatientRequest;
import net.atos.springrestclient.patient.api.PatientResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class ClientRestTemplate {

    private static final String DB_PATIENTS = "http://localhost:8090/api/db/patients";
    //Ctrl+Alt+V przypisanie do zmiennej lokalnej
    RestTemplate restTemplate = new RestTemplate();

    public void listAllPatient() {

        // wywołanie metody GET
        // w paramterach podejemy tylko adres URL oraz zwracany obiekt
        List<PatientResponse> listPatient = restTemplate.getForObject("http://localhost:8090/api/memory/patients", List.class);

        if (!listPatient.isEmpty()) {
            log.info("All patient {}", listPatient);
        }
        else {
            log.info("Patient list is empty");
        }
    }

    public void listAllPatientDb() {

        HttpHeaders httpHeaders = prepareHttpHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        // wywołanie metody GET
        // w paramterach podejemy tylko adres URL oraz zwracany obiekt
        ResponseEntity<List<PatientResponse>> exchange = restTemplate.exchange(DB_PATIENTS,
                                                                               HttpMethod.GET,
                                                                               httpEntity,
                                                                               new ParameterizedTypeReference<List<PatientResponse>>() {});
        log.info("Status: {}", exchange.getStatusCode());
        if (HttpStatus.OK.equals(exchange.getStatusCode())) {
            log.info("All db patient {}", exchange.getBody());
        }
    }

    private HttpHeaders prepareHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("admin", "abc123", StandardCharsets.UTF_8));
        return httpHeaders;
    }

    void getOnePatientByIdDb(long id) {
        //Ctrl+Alt+C export do stałej w klasie
        ResponseEntity<PatientResponse> exchange = restTemplate.exchange(DB_PATIENTS + "/" + id,
                                                                         HttpMethod.GET,
                                                                         new HttpEntity<>(prepareHttpHeaders()),
                                                                         PatientResponse.class);

        HttpStatus statusCode = exchange.getStatusCode();
        log.info("Status: {}", statusCode);
        if (HttpStatus.OK.equals(statusCode)) {
            log.info("Pacjent o id: {} to {}", id, exchange.getBody());
            return;
        }
        if (HttpStatus.NOT_FOUND.equals(statusCode)) {
            log.info("Pacjent o id {} nie istnieje", id);
            return;
        }

        log.info("Błąd pobierania pacjenta");
    }

    void createPatientDb(PatientRequest request) {
        HttpEntity<PatientRequest> entityRequest = new HttpEntity<>(request, prepareHttpHeaders());

        URI uri = restTemplate.postForLocation(DB_PATIENTS, entityRequest, PatientResponse.class);
        log.info("Lokalizacja dodanego obiektu {}", uri.toASCIIString());
    }

    void updatePatientDb(long id, PatientRequest patientRequest) {

        HttpEntity<PatientRequest> entityRequest = new HttpEntity<>(patientRequest, prepareHttpHeaders());
        ResponseEntity<PatientResponse> exchange = restTemplate.exchange(DB_PATIENTS + "/" + id, HttpMethod.PUT, entityRequest, PatientResponse.class);

        HttpStatus statusCode = exchange.getStatusCode();
        log.info("Status: {}", statusCode);
        if (HttpStatus.OK.equals(statusCode)) {
            log.info("Pacjent o id: {} to {}", id, exchange.getBody());
        }
    }

    void deletePatientDb(long id) {
        ResponseEntity<String> exchange = restTemplate.exchange(DB_PATIENTS + "/" + id, HttpMethod.DELETE, new HttpEntity<>(prepareHttpHeaders()), String.class);
        log.info("Wynik usuwania  {}", exchange.getBody());
    }

}
