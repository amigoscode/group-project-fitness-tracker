package com.project.trackfit.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.project.trackfit.customer.Customer;
import com.project.trackfit.media.Media;
import com.project.trackfit.media.MediaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageService {

    private AmazonS3 s3Client;
    private MediaRepository mediaRepository;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public Media uploadImage(MultipartFile image, Customer customer) throws IOException {
        String key = generateKey(image.getOriginalFilename());
        saveImageToS3(image, key);

        Media media = new Media();
        media.setId(UUID.randomUUID());
        media.setType(image.getContentType());
        media.setDate(LocalDateTime.now());
        media.setCustomer(customer);

        mediaRepository.save(media);

        return media;
    }

    private String generateKey(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

    private void saveImageToS3(MultipartFile image, String key) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(getBucketName(), key, image.getInputStream(), metadata);
        s3Client.putObject(putObjectRequest);
    }

    public S3Object getImage(UUID mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new IllegalArgumentException("Media not found with id: " + mediaId));

        return s3Client.getObject(getBucketName(), media.getId().toString());
    }

    private String getBucketName() {
        return bucketName;
    }
}
