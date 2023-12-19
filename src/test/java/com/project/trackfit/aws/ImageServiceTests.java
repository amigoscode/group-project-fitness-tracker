package com.project.trackfit.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.service.CustomerService;
import com.project.trackfit.media.Media;
import com.project.trackfit.media.MediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTests {

    @Mock
    private AmazonS3 s3Client;

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private ImageService imageService;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(imageService, "bucketName", "test-bucket");
    }

    @Test
    @DisplayName("Unit test for uploading an image")
    void uploadImageTest() throws IOException {
        MultipartFile image = mock(MultipartFile.class);
        Customer customer = new Customer();
        String originalFilename = "test-image.jpg";

        when(image.getOriginalFilename()).thenReturn(originalFilename);
        when(image.getContentType()).thenReturn("image/jpeg");
        when(image.getSize()).thenReturn(1000L);
        when(image.getInputStream()).thenReturn(null);

        imageService.uploadImage(image, customer);

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class));
        verify(mediaRepository, times(1)).save(any(Media.class));
    }

    @Test
    @DisplayName("Unit test for getting an image")
    void getImageTest() {
        UUID mediaId = UUID.randomUUID();
        Media media = new Media();
        media.setId(mediaId);
        media.setType("image/jpeg");
        media.setDate(LocalDateTime.now());

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(media));
        when(s3Client.getObject(bucketName, media.getId().toString())).thenReturn(new S3Object());

        imageService.getImage(mediaId);

        verify(mediaRepository, times(1)).findById(mediaId);
        verify(s3Client, times(1)).getObject(bucketName, media.getId().toString());
    }
}
