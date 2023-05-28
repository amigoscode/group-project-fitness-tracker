package com.project.trackfit.aws;

import com.amazonaws.services.s3.model.S3Object;
import com.project.trackfit.media.Media;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ImageControllerTests {

    private ImageController imageController;
    private ImageService imageService;

    @BeforeEach
    public void setUp() {
        imageService = mock(ImageService.class);
        imageController = new ImageController(imageService);
    }

    @Test
    @DisplayName("Unit test for uploading an image")
    public void testUploadImage() throws IOException {
        UUID customerId = UUID.randomUUID();

        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]);

        Media media = new Media();
        media.setId(UUID.randomUUID());
        media.setType("image/jpeg");
        media.setDate(LocalDateTime.now());

        when(imageService.uploadImageForCustomer(customerId, image)).thenReturn(media);

        ResponseEntity<Media> response = imageController.uploadImage(customerId, image);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody(), media);

        verify(imageService).uploadImageForCustomer(customerId, image);
    }

    @Test
    @DisplayName("Unit test for getting an image")
    public void testGetImage() {
        UUID mediaId = UUID.randomUUID();
        S3Object s3Object = new S3Object();

        when(imageService.getImage(mediaId)).thenReturn(s3Object);

        ResponseEntity<S3Object> response = imageController.getImage(mediaId);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), s3Object);

        verify(imageService).getImage(mediaId);
    }
}
