package net.atos.springrestclient.patient;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import net.atos.springrestclient.patient.api.PatientResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ClientWebClient {

    public void listAllPatient() {

        ClientHttpConnector httpConnector = new ReactorClientHttpConnector(HttpClient.create()
                                                                                     .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)));
        WebClient webClient = WebClient.builder()
                                       .clientConnector(httpConnector)
                                       .baseUrl("http://localhost:8090")
                                       .defaultHeader("Authorization", "Basic " + Base64Utils.encodeToString("admin:abc123".getBytes(StandardCharsets.UTF_8)))
                                       .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("HEADER_1", "value_1");

        Mono<ClientResponse> responseMono = webClient.method(HttpMethod.GET)
                                                     .uri("/api/db/patients")
                                                     .headers(headers -> httpHeaders.forEach(headers::put))
                                                     .exchange()
                                                     .onErrorResume(throwable -> {
                                                         throw new RuntimeException("ERROR_CONNECTION");
                                                     })
                                                     .doOnNext(clientResponse -> log.info("CLIENT_RESPONSE {}", clientResponse.statusCode()));

        ClientResponse clientResponse = responseMono.block();

        HttpStatus httpStatus = clientResponse.statusCode();
        log.info("Status {}", httpStatus);
        if (clientResponse.statusCode()
                          .is2xxSuccessful()) {

            ParameterizedTypeReference<List<PatientResponse>> typeReference = new ParameterizedTypeReference<List<PatientResponse>>() {};
            Mono<List<PatientResponse>> listMono = clientResponse.bodyToMono(typeReference);
            List<PatientResponse> patientResponseList = listMono.block();
            log.info("Lista pacjentów {}", patientResponseList);
        }

    }

    public void listAllPatient2Example() {

        ClientHttpConnector httpConnector = new ReactorClientHttpConnector(HttpClient.create()
                                                                                     .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)));
        WebClient webClient = WebClient.builder()
                                       .clientConnector(httpConnector)
                                       .baseUrl("http://localhost:8090")
                                       .defaultHeader("Authorization", "Basic " + Base64Utils.encodeToString("admin:abc123".getBytes(StandardCharsets.UTF_8)))
                                       .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("HEADER_1", "value_1");

        ParameterizedTypeReference<List<PatientResponse>> typeReference = new ParameterizedTypeReference<List<PatientResponse>>() {};

        List<PatientResponse> patientResponseList = webClient.method(HttpMethod.GET)
                                                             .uri("/api/db/patients")
                                                             .headers(headers -> httpHeaders.forEach(headers::put))
                                                             .exchange()
                                                             .onErrorResume(throwable -> {
                                                                 throw new RuntimeException("ERROR_CONNECTION");
                                                             })
                                                             .doOnNext(clientResponse -> log.info("CLIENT_RESPONSE {}", clientResponse.statusCode()))
                                                             .filter(clientResponse -> clientResponse.statusCode()
                                                                                                     .is2xxSuccessful())
                                                             .flatMap(clientResponse -> clientResponse.bodyToMono(typeReference))
                                                             .defaultIfEmpty(Collections.emptyList())
                                                             .block();

        log.info("Lista pacjentów {}", patientResponseList);
    }

}
