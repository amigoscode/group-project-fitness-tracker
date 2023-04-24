package com.project.trackfit.measurements;

import com.project.trackfit.core.APICustomResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;


    /**
     * Spring Boot REST API creates measurements
     * http://[::1]:8080/api/v1/measurements
     */
    @PostMapping
    public ResponseEntity<APICustomResponse> createMeasurements(@Valid @RequestBody CreateMeasurementsRequest createMeasurementsRequest) {
        UUID measurementsId = measurementsService.createMeasurements(createMeasurementsRequest);
        Map<String, UUID> data = new HashMap<>();
        data.put("Measurements_Id", measurementsId);
        return new ResponseEntity(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(data)
                        .message("Measurements have been created successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build(),
                CREATED
        );
    }

    /**
     * Spring Boot REST API gets Measurements by Id
     * http://[::1]:8080/api/v1/measurements/{id}
     */
    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getMeasurementsById(@PathVariable("id") UUID measurementsId) {
        RetrieveMeasurementsRequest measurementsRequest = measurementsService.retrieveMeasurementsById(measurementsId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("measurements", measurementsRequest))
                        .message("Measurements have been fetched successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Spring Boot REST API gets all measurements by customerId
     * http://[::1]:8080/api/v1/measurements/customer/{customerId}
     */
    @GetMapping("customer/{customerId}")
    public ResponseEntity<APICustomResponse> getCustomerMeasurements(@PathVariable("customerId") UUID customerId) {
        List<RetrieveMeasurementsRequest> customerMeasurements = measurementsService.getCustomerMeasurements(customerId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .data(Map.of("customerMeasurements", customerMeasurements))
                        .message("Customer measurements have been retrieved successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Spring Boot REST API updates a measurement by Id
     * http://[::1]:8080/api/v1/measurements/customer/{customerId}
     */
    @PutMapping("/customer/{customerId}")
    public ResponseEntity<APICustomResponse> updateCustomerMeasurements(
            @PathVariable("customerId") UUID customerId,
            @Valid @RequestBody CreateMeasurementsRequest createMeasurementsRequest) throws ParseException {
        measurementsService.updateCustomerMeasurements(customerId, createMeasurementsRequest);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .message("Measurements have been updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Spring Boot REST API deletes a measurement by Id
     * http://[::1]:8080/api/v1/measurements/{measurementId}
     */
    @DeleteMapping("{measurementId}")
    public ResponseEntity<APICustomResponse> deleteMeasurementById(@PathVariable("measurementId") UUID measurementId) {
        measurementsService.deleteMeasurementById(measurementId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(now())
                        .message("Measurement has been deleted successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}