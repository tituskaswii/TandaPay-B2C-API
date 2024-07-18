package com.tandapay.b2c.b2c_api.service;

import com.tandapay.b2c.b2c_api.exception.BadRequestException;
import com.tandapay.b2c.b2c_api.exception.ResourceNotFoundException;
import com.tandapay.b2c.b2c_api.model.B2CRequest;
import com.tandapay.b2c.b2c_api.repository.B2CRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class B2CService {
    private final B2CRequestRepository b2cRequestRepository;
    private final KafkaProducer kafkaProducer;

    public B2CRequest processB2CRequest(B2CRequest request) {
        if (request.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than zero.");
        }
        // Additional validation logic here

        request.setStatus("Pending");
        B2CRequest savedRequest = b2cRequestRepository.save(request);
        kafkaProducer.sendMessage("b2c-request", savedRequest);
        return savedRequest;
    }

    public B2CRequest getB2CRequestStatus(String id) {
        return b2cRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + id));
    }

    public B2CRequest updateB2CRequestStatus(String id, String status) {
        Optional<B2CRequest> optionalRequest = b2cRequestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            B2CRequest request = optionalRequest.get();
            request.setStatus(status);
            return b2cRequestRepository.save(request);
        } else {
            throw new ResourceNotFoundException("Request not found with id: " + id);
        }
    }
}