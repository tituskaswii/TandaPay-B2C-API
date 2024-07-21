package com.tandapay.b2c.b2c_api.UnitTests;

import com.tandapay.b2c.b2c_api.model.B2CRequest;
import com.tandapay.b2c.b2c_api.model.B2CResponse;
import com.tandapay.b2c.b2c_api.repository.B2CRequestRepository;
import com.tandapay.b2c.b2c_api.service.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class KafkaConsumerTests {

    @Mock
    private B2CRequestRepository b2cRequestRepository;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void listenTest() {
        B2CResponse response = new B2CResponse();
        response.setConversationID("AG_20191219_00005797af5d7d75f652");
        response.setOriginatorConversationID("16740-34861180-1");
        response.setResponseCode("0");
        response.setResponseDescription("Accept the service request successfully.");

        B2CRequest request = new B2CRequest();
        request.setOriginatorConversationID("16740-34861180-1");
        request.setStatus("Pending");

        when(b2cRequestRepository.findByOriginatorConversationID(response.getOriginatorConversationID()))
                .thenReturn(Optional.of(request));

        kafkaConsumer.listen(response);

        verify(b2cRequestRepository, times(1)).save(any(B2CRequest.class));
    }
}
