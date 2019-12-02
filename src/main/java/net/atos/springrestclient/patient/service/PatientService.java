package net.atos.springrestclient.patient.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.atos.springrestclient.patient.api.PatientRequest;
import net.atos.springrestclient.patient.api.PatientResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PatientService {

    private ServiceAdapter serviceAdapter;

    public Mono<List<PatientResponse>> getPatientWithNameAdam() {
        return serviceAdapter.getAllPatients()
                             .map(patient -> patient.stream()
                                                    .filter(p -> "Adam".equalsIgnoreCase(p.getName()))
                                                    .collect(Collectors.toList()))
                             .doOnNext(p -> log.info("Pacjent: {}", p));
    }

}
