package com.project.trackfit.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.CustomerController;
import com.project.trackfit.customer.CustomerResponse;
import com.project.trackfit.customer.Customer;
import com.project.trackfit.customer.CustomerUpdateRequest;
import com.project.trackfit.customer.ICustomerService;
import com.project.trackfit.security.jwt.JwtRequestFilter;
import com.project.trackfit.user.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import static com.project.trackfit.user.Role.CUSTOMER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
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

    @Autowired
    private ObjectMapper objectMapper;

    private EasyRandom easyRandom;

    @BeforeEach
    public void setup() {
        EasyRandomParameters parameters =
                new EasyRandomParameters()
                        .randomize(FieldPredicates.named("age")
                        .and(FieldPredicates.inClass(CustomerResponse.class)
                                .or(FieldPredicates.inClass(User.class))),
                        () -> new Random().nextInt(83) + 18);
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    @DisplayName("Successfully get a customer by his unique Id")
    public void givenCustomer_whenGetCustomerById_thenReturnCustomer() throws Exception {
        //given: a random customer
        CustomerResponse customer = new CustomerResponse(
                UUID.randomUUID(),
                "John",
                "Doe",
                30,
                "john@example.com",
                "123 Street",
                CUSTOMER,
                "00306931234567"
        );

        //and: mocking the service to return this random customer
        given(service.getCustomerById(customer.id())).willReturn(customer);

        //when: trying to fetch this random customer
        ResultActions response = mockMvc.perform(get("/api/v1/customers/{id}", customer.id()));

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

    @Test
    @DisplayName("Successfully update a customer")
    public void givenSavedCustomer_whenUpdateCustomer_thenReturnUpdatedCustomer() throws Exception {
        //given: a random customer
        CustomerResponse savedCustomer = new CustomerResponse(
                UUID.randomUUID(),
                "John",
                "Doe",
                30,
                "john@example.com",
                "123 Street",
                CUSTOMER,
                "00306931234567"
        );

        //and: an updated customer
        CustomerResponse updatedCustomer = new CustomerResponse(
                savedCustomer.id(),
                savedCustomer.firstName(),
                savedCustomer.lastName(),
                35,
                savedCustomer.email(),
                "456 New Street",
                savedCustomer.role(),
                "00306931234567"
        );

        //and: mocking the service to return this random customer
        given(service.getCustomerById(savedCustomer.id())).willReturn(savedCustomer);

        //and: mocking the service to update this customer
        given(service.updateCustomer(eq(savedCustomer.id()), any(CustomerUpdateRequest.class)))
                .willReturn(updatedCustomer);

        //when: sending the request
        ResultActions response = mockMvc.perform(put("/api/v1/customers/{id}", savedCustomer.id())
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

    @Test
    @DisplayName("Not found status code when updating a non-existing customer")
    public void givenNonExistingCustomerUUID_whenUpdateCustomer_thenReturnIsNotFound() throws Exception {
        //given: a random UUID for a non-existing customer
        UUID nonExistentCustomerId = UUID.randomUUID();

        //and: creating a valid update customer request
        CustomerUpdateRequest validRequest = new CustomerUpdateRequest(30, "Valid Address", null);

        //and: mocking the service to return the proper exception
        given(service.updateCustomer(eq(nonExistentCustomerId), any(CustomerUpdateRequest.class)))
                .willThrow(new ResourceNotFoundException());

        //when: sending the update request for the non-existing customer
        ResultActions response = mockMvc.perform(put("/api/v1/customers/{id}", nonExistentCustomerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)));

        //then: expect a 404 Not Found status
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User Doesn't Exist")));
    }

    @Test
    @DisplayName("Successfully update a customer's age and address")
    public void givenSavedCustomer_whenUpdateCustomerPartially_thenReturnUpdatedCustomer() throws Exception {
        //given: a saved customer
        Customer savedCustomer = easyRandom.nextObject(Customer.class);

        //and: a request with valid age and address
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(35, "New Address", null);

        //and: a CustomerResponse object to be returned by the mocked service
        CustomerResponse updatedCustomer = new CustomerResponse(
                savedCustomer.getId(),
                savedCustomer.getUser().getFirstName(),
                savedCustomer.getUser().getLastName(),
                35, // new age
                savedCustomer.getUser().getEmail(),
                "New Address",
                savedCustomer.getUser().getRole(),
                "00306931234567"
        );

        //and: mock the service method to return the updated customer
        given(service.updateCustomer(eq(savedCustomer.getId()), any(CustomerUpdateRequest.class))).willReturn(updatedCustomer);

        //when: sending the partial update request with valid data
        ResultActions response = mockMvc.perform(put("/api/v1/customers/{id}", savedCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        //then: expect a successful response with status 200 OK along with the new values
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("Customer has been updated successfully")))
                .andExpect(jsonPath("$.data.Customer.age", is(35)))
                .andExpect(jsonPath("$.data.Customer.address", is("New Address")));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAges")
    @DisplayName("Updating customer with invalid age should fail")
    public void givenInvalidAge_whenUpdateCustomer_thenValidationFails(Integer invalidAge, String expectedErrorMessage) throws Exception {
        //given: a request with an invalid age inputs
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(invalidAge, "Valid Address", CUSTOMER);

        //when: calling the service with invalid age
        ResultActions response = mockMvc.perform(put("/api/v1/customers/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)));

        //then: status is 400 Bad Request, with proper expected response fields
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.age", containsInAnyOrder(expectedErrorMessage)));
    }

    static Stream<Arguments> provideInvalidAges() {
        return Stream.of(
                Arguments.of(17, "Age must be at least 18"),
                Arguments.of(101, "Age must be no more than 100")
        );
    }
}
