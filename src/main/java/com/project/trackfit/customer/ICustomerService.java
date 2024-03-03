package com.project.trackfit.customer;

import com.project.trackfit.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ICustomerService {
    UUID createCustomer(User user);
    CustomerResponse getCustomerById(UUID customer_id);
    CustomerResponse updateCustomer(UUID customerId, CustomerUpdateRequest customerUpdateRequest);
    void uploadImage(UUID customerId, MultipartFile image);
    byte[] getImage(UUID customerId, UUID mediaId);
}