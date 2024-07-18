package com.tandapay.b2c.b2c_api.UnitTests;

import com.tandapay.b2c.b2c_api.exception.BadRequestException;
import com.tandapay.b2c.b2c_api.exception.ResourceNotFoundException;
import com.tandapay.b2c.b2c_api.model.B2CRequest;
import com.tandapay.b2c.b2c_api.repository.B2CRequestRepository;
import com.tandapay.b2c.b2c_api.service.B2CService;
import com.tandapay.b2c.b2c_api.service.KafkaProducer;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void processB2CRequestWithInvalidAmountTest() {
        B2CRequest request = new B2CRequest();
        request.setAmount(0);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            b2cService.processB2CRequest(request);
        });

        assertEquals("Amount must be greater than zero.", exception.getMessage());
    }

    @Test
    public void getB2CRequestStatusNotFoundTest() {
        when(b2cRequestRepository.findById("1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            b2cService.getB2CRequestStatus("1");
        });

        assertEquals("Request not found with id: 1", exception.getMessage());
    }
}

