package com.project.trackfit.aws;

import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import com.project.trackfit.media.Media;
import com.project.trackfit.media.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageService {

    private final S3Service s3Service;
    private final MediaRepository mediaRepository;
    private final CustomerRepository customerRepository;
    private final S3Buckets s3Buckets;

    @Autowired
    public ImageService(S3Service s3Service,
                        MediaRepository mediaRepository,
                        CustomerRepository customerRepository,
                        S3Buckets s3Buckets) {
        this.s3Service = s3Service;
        this.mediaRepository = mediaRepository;
        this.customerRepository = customerRepository;
        this.s3Buckets = s3Buckets;
    }

    public void uploadImageForCustomer(UUID customerId, MultipartFile image) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);

        UUID mediaId = UUID.randomUUID();
        String imageKey = String.format("profile-images/%s/%s", customerId, mediaId);

        try {
            s3Service.putObject(
                    s3Buckets.getCustomer(),
                    imageKey,
                    image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Media media = new Media();
        media.setId(mediaId);
        media.setType(image.getContentType());
        media.setType(image.getContentType());
        media.setDate(LocalDateTime.now());
        media.setCustomer(customer);
        media.setKey(imageKey);

        mediaRepository.save(media);
    }

    public byte[] getImage(UUID customerId, UUID mediaId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(ResourceNotFoundException::new);

        if(!media.getCustomer().getId().equals(customer.getId())) {
            throw new ResourceNotFoundException();
        }

        return s3Service.getObject(
                s3Buckets.getCustomer(),
                media.getKey()
        );
    }
}
