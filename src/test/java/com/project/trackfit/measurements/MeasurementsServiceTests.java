package com.project.trackfit.measurements;

import com.project.trackfit.core.exception.MeasurementNotFoundException;
import com.project.trackfit.core.exception.ResourceNotFoundException;
import com.project.trackfit.customer.dto.Customer;
import com.project.trackfit.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeasurementsServiceTests {

    @InjectMocks
    private MeasurementsService measurementsService;

    @Mock
    private MeasurementsRepository measurementsRepository;

    @Mock
    private MeasurementsRetrieveRequestMapper measurementsRetrieveRequestMapper;

    @Mock
    private CustomerRepository customerRepository;

    private RetrieveMeasurementsRequest retrieveMeasurementsRequest;
    private CreateMeasurementsRequest createMeasurementsRequest;
    private UUID customerId;
    private UUID measurementId;
    private Customer customer;
    private Measurements measurement;
    private String height;
    private String weight;
    private String dateString;
    private Date date;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        measurementId = UUID.randomUUID();
        customer = new Customer();
        measurement = new Measurements();

        retrieveMeasurementsRequest = new RetrieveMeasurementsRequest(
                measurementId,
                height,
                weight,
                LocalDateTime.now()
        );

        createMeasurementsRequest = new CreateMeasurementsRequest(
                "1.70",
                "70",
                LocalDateTime.now(),
                customerId,
                null
        );
    }

    @Test
    @DisplayName("Should create measurements with the given request")
    void testCreateMeasurements() throws ParseException {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        measurementsService.createMeasurements(createMeasurementsRequest);
        verify(customerRepository).findById(customerId);
        verify(measurementsRepository).save(any(Measurements.class));
    }

    @Test
    @DisplayName("Should return exception when customer doesn't exist")
    void testCreateMeasurements_customerNotFound() throws ParseException {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(
                ResourceNotFoundException.class,
                () -> measurementsService.createMeasurements(createMeasurementsRequest));
    }

    @Test
    @DisplayName("Should return the customer measurements")
    void testGetCustomerMeasurements() {
        Set<Measurements> measurementsSet = new HashSet<>();
        measurementsSet.add(measurement);
        customer.setMeasurements(measurementsSet);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(measurementsRetrieveRequestMapper.apply(measurement)).thenReturn(retrieveMeasurementsRequest);

        List<RetrieveMeasurementsRequest> result = measurementsService.getCustomerMeasurements(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(measurementId, result.get(0).id());
        assertEquals(height, result.get(0).height());
        assertEquals(weight, result.get(0).weight());

        verify(customerRepository).findById(customerId);
        verify(measurementsRetrieveRequestMapper).apply(measurement);
    }


    @Test
    @DisplayName("Should throw exception when customer not found")
    void testGetCustomerMeasurements_customerNotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> measurementsService.getCustomerMeasurements(customerId));
    }

    @Test
    @DisplayName("Should call the update method")
    void testUpdateCustomerMeasurements() throws ParseException {
        CreateMeasurementsRequest request = new CreateMeasurementsRequest(
                "1.80",
                "75",
                LocalDateTime.now(),
                customerId,
                measurementId
        );

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(measurementsRepository.findById(measurementId)).thenReturn(Optional.of(measurement));

        measurementsService.updateCustomerMeasurements(customerId, request);

        verify(customerRepository).findById(customerId);
        verify(measurementsRepository).findById(measurementId);
        verify(measurementsRepository).save(measurement);
    }

    @Test
    @DisplayName("Should throw exception when customer not found")
    void testUpdateCustomerMeasurements_customerNotFound() throws ParseException {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(
                ResourceNotFoundException.class,
                () -> measurementsService.updateCustomerMeasurements(customerId, createMeasurementsRequest)
        );
    }

    @Test
    @DisplayName("Should throw exception when measurement not found")
    void testUpdateCustomerMeasurements_measurementNotFound() throws ParseException {
        CreateMeasurementsRequest request = new CreateMeasurementsRequest(
                "1.80",
                "75",
                LocalDateTime.now(),
                customerId,
                measurementId
        );

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(measurementsRepository.findById(measurementId)).thenReturn(Optional.empty());

        assertThrows(
                MeasurementNotFoundException.class,
                () -> measurementsService.updateCustomerMeasurements(customerId, request)
        );
    }

    @Test
    @DisplayName("Should retrieve measurement by Id")
    void testRetrieveMeasurementsById() {
        when(measurementsRepository.findById(measurementId)).thenReturn(Optional.of(measurement));
        when(measurementsRetrieveRequestMapper.apply(measurement)).thenReturn(retrieveMeasurementsRequest);

        RetrieveMeasurementsRequest result = measurementsService.retrieveMeasurementsById(measurementId);

        assertNotNull(result);
        assertEquals(measurementId, result.id());
        assertEquals(height, result.height());
        assertEquals(weight, result.weight());

        verify(measurementsRepository).findById(measurementId);
        verify(measurementsRetrieveRequestMapper).apply(measurement);
    }

    @Test
    @DisplayName("Should return exception when measurement doesn't exist")
    void testRetrieveMeasurementsById_notFound() {
        when(measurementsRepository.findById(measurementId)).thenReturn(Optional.empty());
        assertThrows(
                MeasurementNotFoundException.class,
                () -> measurementsService.retrieveMeasurementsById(measurementId)
        );
    }

    @Test
    @DisplayName("Should delete the measurement found by the repository")
    void testDeleteMeasurementById() {
        when(measurementsRepository.findById(measurementId)).thenReturn(Optional.of(measurement));
        measurementsService.deleteMeasurementById(measurementId);
        verify(measurementsRepository).findById(measurementId);
        verify(measurementsRepository).delete(measurement);
    }

    @Test
    @DisplayName("Should return exception when measurement doesn't exist")
    void testDeleteMeasurementById_notFound() {
        when(measurementsRepository.findById(measurementId)).thenReturn(Optional.empty());
        assertThrows(
                MeasurementNotFoundException.class,
                () -> measurementsService.deleteMeasurementById(measurementId)
        );
    }
}