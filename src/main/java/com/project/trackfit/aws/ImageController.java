package com.project.trackfit.aws;

import com.project.trackfit.core.APICustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(
            value = "{customerId}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<APICustomResponse> uploadImage(
            @PathVariable("customerId") UUID customerId,
            @RequestParam("image") MultipartFile image
    ) {
        imageService.uploadImageForCustomer(customerId, image);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .message("Image uploaded successfully")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("{customerId}/{mediaId}")
    public ResponseEntity<APICustomResponse> getImage(
            @PathVariable("customerId") UUID customerId,
            @PathVariable("mediaId") UUID mediaId
    ) {
        byte[] imageData = imageService.getImage(customerId, mediaId);
        return ResponseEntity.ok(
                APICustomResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Image retrieved successfully")
                        .data(imageData)
                        .build()
        );
    }
}
