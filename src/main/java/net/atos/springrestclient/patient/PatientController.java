package net.atos.springrestclient.patient;

import lombok.AllArgsConstructor;
import net.atos.springrestclient.patient.api.PatientResponse;
import net.atos.springrestclient.patient.service.PatientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/patients")
public class PatientController {

    private PatientService patientService;

    @GetMapping()
    public DeferredResult<List<PatientResponse>> getPatients() {

        DeferredResult<List<PatientResponse>> result = new DeferredResult<>();

        patientService.getPatientWithNameAdam()
                      .subscribe(result::setResult, result::setErrorResult);
        return result;
    }

}
