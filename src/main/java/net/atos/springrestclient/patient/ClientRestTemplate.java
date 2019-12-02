package net.atos.springrestclient.patient;

import lombok.extern.slf4j.Slf4j;
import net.atos.springrestclient.patient.api.PatientResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class ClientRestTemplate {



    public void listAllPatient(){
        //Ctrl+Alt+V przypisanie do zmiennej lokalnej
        RestTemplate restTemplate = new RestTemplate();

        // wywołanie metody GET
        // w paramterach podejemy tylko adres URL oraz zwracany obiekt
        List<PatientResponse> listPatient = restTemplate.getForObject("http://localhost:8090/api/memory/patients", List.class);

        if (!listPatient.isEmpty()){
            log.info("All patient {}", listPatient);
        } else {
            log.info("Patient list is empty");
        }

    }

}
