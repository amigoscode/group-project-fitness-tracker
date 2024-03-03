package com.project.trackfit.aws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    private S3Service s3Service;

    private String bucket;
    private String key;
    private byte[] data;

    @BeforeEach
    void setUp() {
        bucket = "customer";
        key = "foo";
        data = "Hello World".getBytes();
        s3Service = new S3Service(s3Client);
    }

    @Test
    @DisplayName("Successfully put an object into an AWS S3 Bucket")
    void givenBucketNameKeyData_whenPutAnObject_thenObjectGetsPutIntoBucket() throws IOException {
        //when: calling the service to put an object into the S3 bucket
        s3Service.putObject(bucket, key, data);

        //and: capture the arguments passed to the putObject method
        ArgumentCaptor<PutObjectRequest> putObjectRequestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        ArgumentCaptor<RequestBody> requestBodyCaptor = ArgumentCaptor.forClass(RequestBody.class);

        //then: the client has been called
        verify(s3Client).putObject(putObjectRequestCaptor.capture(), requestBodyCaptor.capture());

        //and: extract the captured values for assertions
        PutObjectRequest putObjectRequestCaptorValue = putObjectRequestCaptor.getValue();
        RequestBody requestBodyCaptorValue = requestBodyCaptor.getValue();

        //and: the bucket and key match the expected values
        assertThat(putObjectRequestCaptorValue.bucket()).isEqualTo(bucket);
        assertThat(putObjectRequestCaptorValue.key()).isEqualTo(key);

        //and: the expected data matches the initial one
        assertThat(requestBodyCaptorValue.contentStreamProvider().newStream().readAllBytes())
                .isEqualTo(RequestBody.fromBytes(data).contentStreamProvider().newStream().readAllBytes());
    }

    @Test
    @DisplayName("Successfully get an object from AWS S3 Bucket")
    void givenBucketNameKeyData_whenGetAnObject_thenReturnObject() throws IOException {
        //given: a request for getting an object
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        //and: a response with the expected data
        ResponseInputStream<GetObjectResponse> response = mock(ResponseInputStream.class);
        given(response.readAllBytes()).willReturn(data);

        //and: mocking the client to return this response
        given(s3Client.getObject(eq(getObjectRequest))).willReturn(response);

        //when: calling the service to get an object from the S3 bucket
        byte[] bytes = s3Service.getObject(bucket, key);

        //then: the retrieved bytes match the expected data
        assertThat(bytes).isEqualTo(data);
    }

    @Test
    @DisplayName("Getting an object from AWS S3 Bucket throws exception")
    void givenBucketNameKeyData_whenGetAnObject_willThrowException() throws IOException {
        //when: using a request for getting an object
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        //and: a response that will throw an exception
        ResponseInputStream<GetObjectResponse> response = mock(ResponseInputStream.class);
        given(response.readAllBytes()).willThrow(new IOException("Cannot read bytes"));

        //and: mocking the client to return this response
        given(s3Client.getObject(eq(getObjectRequest))).willReturn(response);

        //then: the service will throw the exception with a specific message and root cause
        assertThatThrownBy(() -> s3Service.getObject(bucket, key))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cannot read bytes")
                .hasRootCauseInstanceOf(IOException.class);
    }
}
