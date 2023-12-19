package com.project.trackfit.customer.controller;

import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.service.ICustomerService;
import com.project.trackfit.security.jwt.JwtRequestFilter;
import com.project.trackfit.user.dto.CreateUserRequest;
import com.project.trackfit.user.service.IApplicationUserService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CustomerController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtRequestFilter.class
                ),
        })
public class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICustomerService service;

    @MockBean
    private IApplicationUserService userService;

    @Test
    @DisplayName("Successfully get a customer by his unique Id")
    public void givenUserRequest_whenCreateUser_thenReturnSavedUser() throws Exception {
        //given: a random UUID
        UUID customer_id = UUID.randomUUID();

        //and: creating a random customer
        EasyRandom easyRandom = new EasyRandom();
        Customer randomCustomer = easyRandom.nextObject(Customer.class);

        //and: mocking the service to create a user with is UUID
        given(userService.createUser(any(CreateUserRequest.class))).willAnswer((invocation) -> customer_id);

        //and: mocking the service to return this random customer
        given(service.getCustomerById(customer_id)).willReturn(randomCustomer);

        //when: trying to fetch this random customer
        ResultActions response = mockMvc.perform(get("/api/v1/customers/{id}", customer_id));

        //then: the response is OK with expected values
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("Customer has been fetched successfully")))
                .andExpect(jsonPath("$.data", notNullValue()));
    }
}
