package com.tandapay.b2c.b2c_api.service;

import com.tandapay.b2c.b2c_api.model.B2CResponse;
import com.tandapay.b2c.b2c_api.repository.B2CRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    @SuppressWarnings("unused")
    private final B2CRequestRepository b2cRequestRepository;

    @KafkaListener(topics = "b2c-response", groupId = "b2c-consumer-group")
    public void listen(B2CResponse response) {
        log.info("Received response: {}", response);
        // Process the response and update the MongoDB
    }
}
