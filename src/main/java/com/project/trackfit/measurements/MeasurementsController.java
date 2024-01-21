package com.project.trackfit.measurements;

import com.project.trackfit.core.APICustomResponse;
import jakarta.validation.Valid;
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

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;

    public MeasurementsController(MeasurementsService measurementsService) {
        this.measurementsService = measurementsService;
    }

    @PostMapping
    public ResponseEntity<APICustomResponse> createMeasurements(
            @Valid @RequestBody MeasurementsRequest measurementsRequest) {
        UUID measurementsId = measurementsService.createMeasurements(measurementsRequest);

        return ResponseEntity.status(CREATED)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("Measurements_Id", measurementsId))
                        .message("Measurements have been created successfully")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
                );
    }

    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getMeasurementsById(
            @PathVariable("id") UUID measurementsId) {
        MeasurementsResponse measurementsRequest = measurementsService.retrieveMeasurementsById(measurementsId);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("measurements", measurementsRequest))
                        .message("Measurements have been fetched successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    @PutMapping("{customerId}")
    public ResponseEntity<APICustomResponse> updateCustomerMeasurements(
            @PathVariable("customerId") UUID customerId,
            @Valid @RequestBody MeasurementsRequest measurementsRequest) {
        measurementsService.updateCustomerMeasurements(customerId, measurementsRequest);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(null)
                        .message("Measurements have been updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }

    @DeleteMapping("{measurementId}")
    public ResponseEntity<APICustomResponse> deleteMeasurementById(
            @PathVariable("measurementId") UUID measurementId) {
        measurementsService.deleteMeasurementById(measurementId);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(null)
                        .message("Measurement has been deleted successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }
}
