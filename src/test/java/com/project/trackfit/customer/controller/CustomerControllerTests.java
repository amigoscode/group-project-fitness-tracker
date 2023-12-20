package com.project.trackfit.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.dto.UpdateCustomerRequest;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Successfully get a customer by his unique Id")
    public void givenCustomer_whenGetCustomerById_thenReturnCustomer() throws Exception {
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

    @Test
    @DisplayName("Getting a customer fails because he doesn't exist")
    public void givenNonExistingCustomerUUID_whenGetCustomerById_thenReturnIsNotFound() throws Exception {
        //given: a non-existent customer id
        UUID nonExistentCustomerId = UUID.randomUUID();

        //and: mocking the service to return the proper exception
        given(service.getCustomerById(nonExistentCustomerId)).willThrow(new ResourceNotFoundException());

        //when: trying to fetch this customer
        ResultActions response = mockMvc.perform(get("/api/v1/customers/{id}", nonExistentCustomerId));

        //then: the response is Not Found
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User Doesn't Exist")));
    }

    @DisplayName("Successfully update a customer")
    @Test
    public void givenSavedCustomer_whenUpdateCustomer_thenReturnUpdatedCustomer() throws Exception {
        //given: a random UUID
        UUID customer_id = UUID.randomUUID();

        //and: creating a random customer
        EasyRandom easyRandom = new EasyRandom();
        Customer savedCustomer = easyRandom.nextObject(Customer.class);

        //and: an updated customer
        Customer updatedCustomer = easyRandom.nextObject(Customer.class);

        //and: mocking the service to create a user with is UUID
        given(userService.createUser(any(CreateUserRequest.class))).willAnswer((invocation) -> customer_id);

        //and: mocking the service to return this random customer
        given(service.getCustomerById(customer_id)).willReturn(savedCustomer);

        //and: mocking the service to update this customer
        given(service.updateCustomer(eq(customer_id), any(UpdateCustomerRequest.class)))
                .willReturn(updatedCustomer);

        //when: sending the request
        ResultActions response = mockMvc.perform(put("/api/v1/customers/{id}", customer_id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomer)));

        //then: status is OK, with proper expected response fields
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("Customer has been updated successfully")))
                .andExpect(jsonPath("$.data", notNullValue()));
    }
}
