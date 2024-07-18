package com.tandapay.b2c.b2c_api.service;


import com.tandapay.b2c.b2c_api.model.B2CRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, B2CRequest> kafkaTemplate;

    public void sendMessage(String topic, B2CRequest request) {
        kafkaTemplate.send(topic, request);
    }
}

/*@Service
public class KafkaProducer {

    private static final String TOPIC = "b2c-requests";

    @Autowired
    private KafkaTemplate<String, B2CRequest> kafkaTemplate;

    public void sendMessage(B2CRequest request) {
        kafkaTemplate.send(TOPIC, request);
    }
}*/

