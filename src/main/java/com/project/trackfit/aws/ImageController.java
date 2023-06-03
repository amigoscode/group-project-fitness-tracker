package com.project.trackfit.aws;

import com.amazonaws.services.s3.model.S3Object;
import com.project.trackfit.media.Media;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("{customerId}/upload")
    public ResponseEntity<Media> uploadImage(
            @PathVariable("customerId") UUID customerId,
            @RequestParam("image") MultipartFile image) throws IOException {
        Media media = imageService.uploadImageForCustomer(customerId, image);
        return new ResponseEntity<>(media, HttpStatus.CREATED);
    }

    @GetMapping("{mediaId}")
    public ResponseEntity<S3Object> getImage(@PathVariable("mediaId") UUID mediaId) {
        S3Object image = imageService.getImage(mediaId);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}
