package com.project.trackfit.general;

import com.project.trackfit.user.dto.ApplicationUser;
import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GeneralService implements IGeneralService {

    final CustomerService customerService;

    @Override
    public RetrieveGeneralRequest RetrieveHome(ApplicationUser user) {
        final Customer customer = customerService.getCustomerByUserId(user.getId());
        return new RetrieveGeneralRequest(
                customer.getId(),
                customer.getUser().getFirstName(),
                customer.getUser().getLastName(),
                customer.getAge(),
                customer.getUser().getEmail(),
                customer.getAddress()
        );
    }
}
