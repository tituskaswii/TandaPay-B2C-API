package com.tandapay.b2c.b2c_api.service;

import com.tandapay.b2c.b2c_api.model.B2CRequest;
import com.tandapay.b2c.b2c_api.model.B2CResponse;
import com.tandapay.b2c.b2c_api.repository.B2CRequestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final B2CRequestRepository b2cRequestRepository;

    @KafkaListener(topics = "b2c-response", groupId = "b2c-api-group")
    public void consume(B2CResponse response) {
        logger.info("Received response: {}", response);
        // Process the response and update the corresponding B2CRequest
        Optional<B2CRequest> optionalRequest = b2cRequestRepository.findByOriginatorConversationID(response.getOriginatorConversationID());
        if (optionalRequest.isPresent()) {
            B2CRequest request = optionalRequest.get();
            request.setStatus(response.getResponseDescription());
            b2cRequestRepository.save(request);
        } else {
            logger.error("Request not found for OriginatorConversationID: {}", response.getOriginatorConversationID());
        }
    }

    // Method for testing
    public void listen(B2CResponse response) {
        consume(response);
    }
}
