package com.project.trackfit.general;

import com.project.trackfit.core.APICustomResponse;
import com.project.trackfit.user.dto.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1/home")
public class GeneralController {

    final GeneralService generalService;

    @GetMapping
    public ResponseEntity<APICustomResponse> getHome(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        RetrieveGeneralRequest retrieveGeneralRequest = generalService.RetrieveHome(user);

        return ResponseEntity.status(OK)
                .body(APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("user",retrieveGeneralRequest))
                        .message("nothing")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
    }
}
