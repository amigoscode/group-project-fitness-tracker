package com.project.trackfit.customer.repository;

import com.project.trackfit.customer.dto.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

 Optional<Customer> findByUser_Id(UUID id);
}
