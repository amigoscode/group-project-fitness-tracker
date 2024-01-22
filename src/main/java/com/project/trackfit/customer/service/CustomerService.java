package com.project.trackfit.customer.service;

import com.project.trackfit.aws.S3Buckets;
import com.project.trackfit.aws.S3Service;
import com.project.trackfit.core.exception.RequestValidationException;
import com.project.trackfit.customer.dto.CustomerResponse;
import com.project.trackfit.customer.entity.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import com.project.trackfit.customer.dto.UpdateCustomerRequest;
import com.project.trackfit.media.Media;
import com.project.trackfit.media.MediaRepository;
import com.project.trackfit.user.entity.ApplicationUser;
import com.project.trackfit.core.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.project.trackfit.core.mapper.CommonMapper.mapToCustomerResponse;

@Service
public class CustomerService implements ICustomerService {

    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final MediaRepository mediaRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(S3Service s3Service,
                           S3Buckets s3Buckets,
                           MediaRepository mediaRepository,
                           CustomerRepository customerRepository) {
        this.s3Service = s3Service;
        this.s3Buckets = s3Buckets;
        this.mediaRepository = mediaRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public UUID createCustomer(ApplicationUser applicationUser) {
        Customer customer = new Customer(applicationUser);
        customerRepository.save(customer);
        return customer.getId();
    }

    @Override
    public CustomerResponse getCustomerById(UUID userId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        return mapToCustomerResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(
            UUID customerId,
            UpdateCustomerRequest updateRequest) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(ResourceNotFoundException::new);

        boolean changes = false;

        if (updateRequest.getAge() != null && !updateRequest.getAge().equals(customer.getUser().getAge())) {
            customer.getUser().setAge(updateRequest.getAge());
            changes = true;
        }

        if (updateRequest.getAddress() != null && !updateRequest.getAddress().equals(customer.getUser().getAddress())) {
            customer.getUser().setAddress(updateRequest.getAddress());
            changes = true;
        }

        if (updateRequest.getRole() != null && !updateRequest.getRole().equals(customer.getUser().getRole())) {
            customer.getUser().setRole(updateRequest.getRole());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException();
        }

        customerRepository.save(customer);
        return mapToCustomerResponse(customer);
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
