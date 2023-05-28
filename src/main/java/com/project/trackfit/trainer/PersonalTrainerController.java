package com.project.trackfit.trainer;

import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.core.GenericController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/trainers")
@PreAuthorize("isAuthenticated()")
public class PersonalTrainerController extends GenericController {

    private final IPersonalTrainerService IPersonalTrainerService;

    /**
     * Gets all Personal Trainers
     * http://[::1]:8080/api/v1/trainers
     */
    @GetMapping
    public ResponseEntity<APICustomResponse> getAllTrainers() {
        Iterable<RetrieveTrainerRequest> trainers = IPersonalTrainerService.findAllTrainers();
        return createResponse(
                Map.of("Personal Trainers", trainers),
                "Fetched All Personal Trainers",
                OK);
    }

    /**
     * Gets a Trainer by Id
     * http://[::1]:8080/api/v1/trainers/{id}
     */
    @GetMapping("{id}")
    public ResponseEntity<APICustomResponse> getTrainerById(@PathVariable("id") UUID trainerId) {
        RetrieveTrainerRequest trainer = IPersonalTrainerService.retrieveTrainerByID(trainerId);
        return createResponse(
                Map.of("trainer", trainer),
                "Trainer has been fetched successfully",
                OK);
    }
}
