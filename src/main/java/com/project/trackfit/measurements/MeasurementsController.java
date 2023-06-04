package com.project.trackfit.measurements;

import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.core.GenericController;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/measurements")
public class MeasurementsController extends GenericController {

    private final MeasurementsService measurementsService;

    /**
     * Creates measurements
     * http://[::1]:8080/api/v1/measurements
     */
    @PostMapping
    public ResponseEntity<APICustomResponse> createMeasurements(
            @Valid @RequestBody CreateMeasurementsRequest createMeasurementsRequest) {
        UUID measurementsId = measurementsService.createMeasurements(createMeasurementsRequest);
        return createResponse(
                Map.of("Measurements_Id", measurementsId),
                "Measurements have been created successfully",
                CREATED);
    }

    /**
     * Gets Measurements by Id
     * http://[::1]:8080/api/v1/measurements/{id}
     */
    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getMeasurementsById(
            @PathVariable("id") UUID measurementsId) {
        RetrieveMeasurementsRequest measurementsRequest = measurementsService.retrieveMeasurementsById(measurementsId);
        return createResponse(
                Map.of("measurements", measurementsRequest),
                "Measurements have been fetched successfully",
                OK);
    }


    /**
     * Updates a measurement by Id
     * http://[::1]:8080/api/v1/measurements/customer/{customerId}
     */
    @PutMapping("{customerId}")
    public ResponseEntity<APICustomResponse> updateCustomerMeasurements(
            @PathVariable("customerId") UUID customerId,
            @Valid @RequestBody CreateMeasurementsRequest createMeasurementsRequest) {
        measurementsService.updateCustomerMeasurements(customerId, createMeasurementsRequest);
        return createResponse(
                null,
                "Measurements have been updated successfully",
                OK);
    }

    /**
     * Deletes a measurement by Id
     * http://[::1]:8080/api/v1/measurements/{measurementId}
     */
    @DeleteMapping("{measurementId}")
    public ResponseEntity<APICustomResponse> deleteMeasurementById(
            @PathVariable("measurementId") UUID measurementId) {
        measurementsService.deleteMeasurementById(measurementId);
        return createResponse(
                null,
                "Measurement has been deleted successfully",
                OK);
    }
}
