package com.project.trackfit.customer;

import com.project.trackfit.aws.S3Buckets;
import com.project.trackfit.aws.S3Service;
import com.project.trackfit.core.exception.RequestValidationException;
import com.project.trackfit.customer.CustomerResponse;
import com.project.trackfit.customer.Customer;
import com.project.trackfit.customer.CustomerService;
import com.project.trackfit.customer.CustomerUpdateRequest;
import com.project.trackfit.customer.CustomerRepository;
import com.project.trackfit.customer.Media;
import com.project.trackfit.customer.MediaRepository;
import com.project.trackfit.user.User;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.user.Role;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private S3Buckets s3Buckets;

    @Mock
    private MediaRepository mediaRepository;

    @InjectMocks
    private CustomerService customerService;

    private User testUser;

    private EasyRandom easyRandom;

    private final String bucketName = "test-bucket";

    @BeforeEach
    public void setUp() {
        testUser = new User(
                "andreas.kreouzos@hotmail.com",
                "Andreas",
                "Kreouzos",
                new byte[128],
                new byte[64],
                Role.CUSTOMER,
                38,
                "Athens, Greece",
                "00306931234567"
        );
        easyRandom = new EasyRandom();
    }

    @Test
    @DisplayName("Successfully create a customer")
    public void givenUUID_whenCreateCustomer_thenReturnCustomer() {
        //given: the expected customer ID
        UUID expectedCustomerId = UUID.randomUUID();

        //and: mocking the save method to return a customer with this expected ID
        given(customerRepository.save(any(Customer.class))).willAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            savedCustomer.setId(expectedCustomerId);
            return savedCustomer;
        });

        //when: calling the service
        UUID actualCustomerId = customerService.createCustomer(testUser);

        //then: the saving has been completed
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerCaptor.capture());

        //and: retrieve the captured customer
        Customer capturedCustomer = customerCaptor.getValue();

        //and: assert the customer's properties match the application user's properties
        assertNotNull(capturedCustomer);
        assertEquals(testUser.getEmail(), capturedCustomer.getUser().getEmail());
        assertEquals(testUser.getFirstName(), capturedCustomer.getUser().getFirstName());
        assertEquals(testUser.getLastName(), capturedCustomer.getUser().getLastName());
        assertEquals(testUser.getAddress(), capturedCustomer.getUser().getAddress());
        assertEquals(testUser.getAge(), capturedCustomer.getUser().getAge());

        //and: the returned customer ID matches the expected one
        assertEquals(expectedCustomerId, actualCustomerId);
    }

    @Test
    @DisplayName("Getting a customer by ID succeeds")
    public void givenCustomerId_whenGetCustomerById_thenReturnCustomer() {
        //given: a customer ID
        UUID customerId = UUID.randomUUID();

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setUser(testUser);

        //and: an expected customer response
        CustomerResponse expectedCustomerResponse = new CustomerResponse(
                customer.getId(),
                customer.getUser().getFirstName(),
                customer.getUser().getLastName(),
                customer.getUser().getAge(),
                customer.getUser().getEmail(),
                customer.getUser().getAddress(),
                customer.getUser().getRole(),
                customer.getUser().getPhoneNumber()
        );

        //and: mocking the repository to return this customer
        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer));

        //when: calling the service
        CustomerResponse savedCustomerResponse = customerService.getCustomerById(customerId);

        //then: the customer has been found
        verify(customerRepository).findById(customerId);
        assertThat(savedCustomerResponse).isNotNull();
        assertEquals(expectedCustomerResponse, savedCustomerResponse);
    }

    @Test
    @DisplayName("Getting a customer by invalid ID fails")
    public void givenInvalidCustomerId_whenGetCustomerById_thenReturnCustomerNotFound() {
        //given: an invalid customer ID
        UUID invalidCustomerId = UUID.randomUUID();

        //and: mocking the repository to simulate that no customer exists with this ID
        given(customerRepository.findById(invalidCustomerId)).willReturn(Optional.empty());

        //when: calling the service we expect to receive the proper exception
        assertThrows(ResourceNotFoundException.class,
                () -> customerService.getCustomerById(invalidCustomerId));

        //then: we verify the repository interaction
        verify(customerRepository).findById(invalidCustomerId);
    }

    @Test
    @DisplayName("Update customer age and address but not the role, succeeds")
    public void givenCustomerObject_whenUpdateCustomer_thenReturnUpdatedCustomer() {
        //given: a random customer and an update request
        Customer customer = easyRandom.nextObject(Customer.class);
        CustomerUpdateRequest request = new CustomerUpdateRequest(39, "New Address", null);

        //and: mocking the service to simulate that the customer already exists
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //when: updating the customer according to the request
        CustomerResponse updatedCustomer = customerService.updateCustomer(customer.getId(), request);

        //then: the customer's parameters have been updated successfully expect role
        assertThat(updatedCustomer.age()).isEqualTo(request.getAge());
        assertThat(updatedCustomer.address()).isEqualTo(request.getAddress());
        assertThat(updatedCustomer.role()).isEqualTo(customer.getUser().getRole());
    }

    @Test
    @DisplayName("Update customer with same age, address and role fails")
    public void givenInvalidUpdateRequest_whenUpdateCustomer_thenReturnUpdatedCustomer() {
        //given: a random customer and an update request with same property values as customer's
        Customer customer = easyRandom.nextObject(Customer.class);
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                customer.getUser().getAge(),
                customer.getUser().getAddress(),
                customer.getUser().getRole());

        //and: mocking the service to simulate that the customer already exists
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //when: trying to update the customer's properties, an exception is thrown
        assertThrows(RequestValidationException.class,
                () -> customerService.updateCustomer(customer.getId(), request));

        //then: no interactions with the repository take place
        verify(customerRepository, never()).save(customer);
    }

    @Test
    @DisplayName("Uploading an image succeeds")
    public void givenCustomerObject_whenUploadImage_thenSuccess() {
        //given: a random customer
        Customer customer = easyRandom.nextObject(Customer.class);

        //and: mocking the repository to simulate that the customer already exists
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //and: creating an image along with a S3 bucket
        MultipartFile image = new MockMultipartFile("file", "Hello World".getBytes());
        given(s3Buckets.getCustomer()).willReturn(bucketName);

        //when: uploading the image
        customerService.uploadImage(customer.getId(), image);

        //then: the media gets saved
        ArgumentCaptor<Media> mediaCaptor = ArgumentCaptor.forClass(Media.class);
        verify(mediaRepository, times(1)).save(mediaCaptor.capture());

        //and: the media are assigned to this customer
        Media savedMedia = mediaCaptor.getValue();
        assertEquals(customer.getId(), savedMedia.getCustomer().getId());

        //and: the expected content type gets returned
        assertEquals(image.getContentType(), savedMedia.getType());

        //and: check that the key is in the correct format
        String expectedKeyFormat = "profile-images/" + customer.getId() + "/" + savedMedia.getId();
        assertEquals(expectedKeyFormat, savedMedia.getKey());

        //and: verify that s3Service is called with correct parameters
        ArgumentCaptor<String> bucketCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> imageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<byte[]> contentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(s3Service, times(1)).putObject(
                bucketCaptor.capture(),
                imageKeyCaptor.capture(),
                contentCaptor.capture());

        //and: check that the bucket name, imageKey, and content are as expected
        assertEquals(bucketName, bucketCaptor.getValue());
        assertEquals(expectedKeyFormat, imageKeyCaptor.getValue());
        assertArrayEquals("Hello World".getBytes(), contentCaptor.getValue());
    }

    @Test
    @DisplayName("Uploading an image fails due to non existent customer")
    public void givenNonExistentCustomer_whenUploadImage_thenExceptionThrown() {
        //given: a random customer
        Customer customer = easyRandom.nextObject(Customer.class);

        //and: an image file
        MultipartFile image = new MockMultipartFile("file", "Hello World".getBytes());

        //and: mocking the repository to simulate that the customer doesn't exist
        given(customerRepository.findById(customer.getId())).willReturn(Optional.empty());

        //when: uploading the image, a ResourceNotFoundException is expected
        assertThatThrownBy(() ->
                customerService.uploadImage(customer.getId(), image))
                .isInstanceOf(ResourceNotFoundException.class);

        //then: only the customer repository has one interaction
        verify(customerRepository).findById(customer.getId());
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(s3Buckets);
        verifyNoInteractions(s3Service);
    }

    @Test
    @DisplayName("Uploading procedure fails due to invalid file")
    public void givenInvalidFile_whenUpload_thenExceptionThrown() throws IOException {
        //given: a random customer
        Customer customer = easyRandom.nextObject(Customer.class);

        //and: mocking the repository to simulate that the customer already exists
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //and: creating an image along with a S3 bucket
        MultipartFile image = mock(MultipartFile.class);
        given(s3Buckets.getCustomer()).willReturn(bucketName);

        //and: simulating that this image will throw an exception
        given(image.getBytes()).willThrow(IOException.class);

        //when: uploading the image, a RuntimeException is expected
        assertThatThrownBy(() ->
                customerService.uploadImage(customer.getId(), image))
                .isInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(IOException.class);

        //then: no media gets saved
        verify(mediaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Downloading an image succeeds")
    public void givenCustomerAndMedia_whenGetImage_thenImageDownloaded() {
        //given: a random customer
        Customer customer = easyRandom.nextObject(Customer.class);

        //and: a random media
        Media media = easyRandom.nextObject(Media.class);

        //and: set the ownership between this media and customer
        media.setCustomer(customer);

        //and: mocking the customer repository to simulate that the customer already exists
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //and: mocking the media repository to simulate that the media already exists
        given(mediaRepository.findById(media.getId())).willReturn(Optional.of(media));

        //and: mocking the S3 bucket to return the bucket
        given(s3Buckets.getCustomer()).willReturn(bucketName);

        //and: mocking the S3 service to return the image
        given(s3Service.getObject(bucketName, "profile-images/%s/%s".formatted(customer.getId(), media.getId())))
                .willReturn("image".getBytes());

        //when: calling the service
        byte[] actualImage = customerService.getImage(customer.getId(), media.getId());

        //then: the image has been downloaded
        assertThat(actualImage).isEqualTo("image".getBytes());
        verify(mediaRepository, times(1)).findById(media.getId());
    }

    @Test
    @DisplayName("Failed to download an image because no media is present")
    public void givenCustomerButNoMedia_whenGetImage_thenThrowException() {
        //given: a random customer
        Customer customer = easyRandom.nextObject(Customer.class);

        //and: a random media
        Media media = easyRandom.nextObject(Media.class);

        //and: set the ownership between this media and customer
        media.setCustomer(customer);

        //and: mocking the customer repository to simulate that the customer already exists
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //and: mocking the media repository to simulate that the media already exists
        given(mediaRepository.findById(media.getId())).willReturn(Optional.empty());

        //when: calling the service
        assertThatThrownBy(() -> customerService
                .getImage(customer.getId(), media.getId()))
                .isInstanceOf(ResourceNotFoundException.class);

        //then: the image download should not proceed
        verifyNoInteractions(s3Buckets);
        verifyNoInteractions(s3Service);
    }

    @Test
    @DisplayName("Failed to download an image when media does not belong to the customer")
    public void givenCustomerAndMedia_whenMediaDoesNotBelongToCustomer_thenThrowException() {
        //given: a random customer
        Customer customer = easyRandom.nextObject(Customer.class);

        //and: a random media
        Media media = easyRandom.nextObject(Media.class);

        //and: ensuring that media does not belong to the customer initially created
        Customer differentCustomer = new Customer();
        differentCustomer.setId(UUID.randomUUID());
        media.setCustomer(differentCustomer);

        //and: mocking the customer repository to simulate that the customer already exists
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //and: mocking the media repository to simulate that the media already exists
        given(mediaRepository.findById(media.getId())).willReturn(Optional.of(media));

        //when: calling the service
        assertThatThrownBy(() -> customerService.getImage(customer.getId(), media.getId()))
                .isInstanceOf(ResourceNotFoundException.class);

        //then: the image download should not proceed
        verifyNoInteractions(s3Buckets);
        verifyNoInteractions(s3Service);
    }

}
