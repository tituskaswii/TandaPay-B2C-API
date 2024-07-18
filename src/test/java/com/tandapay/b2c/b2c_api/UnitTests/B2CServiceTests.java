package com.tandapay.b2c.b2c_api.UnitTests;

import com.tandapay.b2c.b2c_api.model.B2CRequest;
import com.tandapay.b2c.b2c_api.repository.B2CRequestRepository;
import com.tandapay.b2c.b2c_api.service.B2CService;
import com.tandapay.b2c.b2c_api.service.KafkaProducer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class B2CServiceTests {

    @Mock
    private B2CRequestRepository b2cRequestRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private B2CService b2cService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void processB2CRequestTest() {
        B2CRequest request = new B2CRequest();
        request.setId("1");
        request.setStatus("Pending");

        when(b2cRequestRepository.save(request)).thenReturn(request);

        B2CRequest result = b2cService.processB2CRequest(request);

        assertNotNull(result);
        assertEquals("Pending", result.getStatus());
        verify(b2cRequestRepository, times(1)).save(request);
        verify(kafkaProducer, times(1)).sendMessage("b2c-request", request);
    }

    @Test
    public void getB2CRequestStatusTest() {
        B2CRequest request = new B2CRequest();
        request.setId("1");

        when(b2cRequestRepository.findById("1")).thenReturn(Optional.of(request));

        B2CRequest result = b2cService.getB2CRequestStatus("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void getB2CRequestStatusNotFoundTest() {
        when(b2cRequestRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            b2cService.getB2CRequestStatus("1");
        });

        assertEquals("Request not found", exception.getMessage());
    }

    @Test
    public void updateB2CRequestStatusTest() {
        B2CRequest request = new B2CRequest();
        request.setId("1");
        request.setStatus("Pending");

        when(b2cRequestRepository.findById("1")).thenReturn(Optional.of(request));
        when(b2cRequestRepository.save(request)).thenReturn(request);

        B2CRequest result = b2cService.updateB2CRequestStatus("1", "Completed");

        assertNotNull(result);
        assertEquals("Completed", result.getStatus());
        verify(b2cRequestRepository, times(1)).save(request);
    }

    @Test
    public void updateB2CRequestStatusNotFoundTest() {
        when(b2cRequestRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            b2cService.updateB2CRequestStatus("1", "Completed");
        });

        assertEquals("Request not found", exception.getMessage());
    }
}

