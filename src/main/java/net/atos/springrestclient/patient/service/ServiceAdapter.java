package net.atos.springrestclient.patient.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.atos.springrestclient.patient.api.PatientRequest;
import net.atos.springrestclient.patient.api.PatientResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ServiceAdapter {

    private final WebClient.Builder serverWebClient;

    public Mono<List<PatientResponse>> getAllPatients() {
        return serverWebClient.build()
                              .method(HttpMethod.GET)
                              .uri("/api/db/patients")
                              .exchange()
                              .onErrorResume(throwable -> {
                                  throw new RuntimeException("ERROR_CONNECTION");
                              })
                              .doOnNext(clientResponse -> log.info("Response {}", clientResponse.statusCode()))
                              .flatMap(this::wrapResponse);
    }

    private Mono<List<PatientResponse>> wrapResponse(ClientResponse clientResponse) {
        if (clientResponse.statusCode()
                          .is2xxSuccessful()) {
            ParameterizedTypeReference<List<PatientResponse>> typeReference = new ParameterizedTypeReference<List<PatientResponse>>() {};
            return clientResponse.bodyToMono(typeReference);
        }
        if (clientResponse.statusCode()
                          .is4xxClientError()) {
            throw new RuntimeException("Błąd połączenia");
        }
        return Mono.just(Collections.emptyList());
    }

    public Mono<PatientResponse> createPatient(PatientRequest request) {

       return serverWebClient.build()
                       .method(HttpMethod.POST)
                       .uri("/api/db/patients/v2")
                       .body(BodyInserters.fromPublisher(Mono.just(request), Object.class))
                       //.body(BodyInserters.fromObject(request))//nie wspierane, może zostać usunięte
                       //.body(requestList,new ParameterizedTypeReference<List<PatientRequest>>() {} )
//                       .body(request, PatientRequest.class) - błąd wymaga publisher'a i dodatkowej konfiugracji
                       .exchange()
                       .onErrorResume(
                               throwable -> {
                                   log.error("Wyjątek przy tworzeniu pacjenta", throwable);
                                   throw new RuntimeException("Error connection");

                               })
                       .doOnNext(clientResponse -> log.info("Response {}", clientResponse.statusCode()))
                       .flatMap(this::wrapCreateResponse);
    }

    private Mono<PatientResponse> wrapCreateResponse(ClientResponse clientResponse) {

        if (clientResponse.statusCode()
                          .is2xxSuccessful()) {
            if (HttpStatus.OK.equals(clientResponse.statusCode())) {
                return clientResponse.bodyToMono(PatientResponse.class);
            }
        }
        throw new RuntimeException("Błąd dodawania pacjenta");
    }
}