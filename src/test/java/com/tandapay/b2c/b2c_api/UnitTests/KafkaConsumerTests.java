package com.tandapay.b2c.b2c_api.UnitTests;

import com.tandapay.b2c.b2c_api.model.B2CResponse;
import com.tandapay.b2c.b2c_api.repository.B2CRequestRepository;
import com.tandapay.b2c.b2c_api.service.KafkaConsumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@SuppressWarnings("unused")
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

        kafkaConsumer.listen(response);

        // Add your verification logic here, depending on how the response is processed
    }
}
