package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.entities.MeasurementDTO;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.services.MeasurementService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@CrossOrigin(origins = "*")
@Controller
@RequestMapping(value = "/measurement")
public class MeasurementController {

    @Autowired
    MeasurementService measurementService;
    @Autowired
    private RestTemplate restTemplate;

    private final String authServiceUrl = "http://localhost:8082/measurement";
    @RequestMapping(method = RequestMethod.GET, value = "/auth/all")
    @ResponseBody
    public ResponseEntity<List<Measurement>> getAllAuth() {
        ResponseEntity<Measurement[]> response = restTemplate.exchange(
                authServiceUrl + "/all",
                HttpMethod.GET,
                null,
                Measurement[].class
        );
        return ResponseEntity.ok(Arrays.asList(response.getBody()));
    }
    @RequestMapping(method = RequestMethod.GET, value = "/auth/daily/{date}")
    @ResponseBody
    public ResponseEntity<List<MeasurementDTO>> getAuthDailyMeasurements(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        ResponseEntity<MeasurementDTO[]> response = restTemplate.exchange(
                authServiceUrl + "/daily/" + date,
                HttpMethod.GET,
                null,
                MeasurementDTO[].class
        );
        return ResponseEntity.ok(Arrays.asList(response.getBody()));
    }
}
