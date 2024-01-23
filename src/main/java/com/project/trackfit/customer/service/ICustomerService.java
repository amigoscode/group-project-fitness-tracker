package com.project.trackfit.customer.service;

import com.project.trackfit.customer.dto.CustomerResponse;
import com.project.trackfit.customer.dto.UpdateCustomerRequest;
import com.project.trackfit.user.entity.ApplicationUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ICustomerService {
    UUID createCustomer(ApplicationUser applicationUser);
    CustomerResponse getCustomerById(UUID customer_id);
    CustomerResponse updateCustomer(UUID customerId, UpdateCustomerRequest updateCustomerRequest);
    void uploadImage(UUID customerId, MultipartFile image);
    byte[] getImage(UUID customerId, UUID mediaId);
}