package com.tandapay.b2c.b2c_api.service;

import com.tandapay.b2c.b2c_api.exception.BadRequestException;
import com.tandapay.b2c.b2c_api.exception.ResourceNotFoundException;
import com.tandapay.b2c.b2c_api.model.B2CRequest;
import com.tandapay.b2c.b2c_api.repository.B2CRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class B2CService {
    private final B2CRequestRepository b2cRequestRepository;
    private final KafkaProducer kafkaProducer;

    private static final Pattern KENYA_PHONE_NUMBER_PATTERN = Pattern.compile("^(2547\\d{8}|07\\d{8})$");
    private static final double MIN_AMOUNT = 10.0;
    private static final double MAX_AMOUNT = 150000.0;

    public B2CRequest processB2CRequest(B2CRequest request) {
        validateB2CRequest(request);

        request.setStatus("Pending");
        B2CRequest savedRequest = b2cRequestRepository.save(request);
        kafkaProducer.sendMessage("b2c-request", savedRequest);
        return savedRequest;
    }

    private void validateB2CRequest(B2CRequest request) {
        if (request.getPartyB() == null || request.getPartyB().isEmpty()) {
            throw new BadRequestException("Mobile number (partyB) must be present.");
        }

        if (!KENYA_PHONE_NUMBER_PATTERN.matcher(request.getPartyB()).matches()) {
            throw new BadRequestException("Mobile number (partyB) must be a valid Kenyan and Safaricom number.");
        }

        if (request.getAmount() <= 0 || request.getAmount() < MIN_AMOUNT || request.getAmount() > MAX_AMOUNT) {
            throw new BadRequestException("Amount must be a valid amount between KSh 10 and KSh 150,000.");
        }
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




// package com.tandapay.b2c.b2c_api.service;

// import com.tandapay.b2c.b2c_api.exception.BadRequestException;
// import com.tandapay.b2c.b2c_api.exception.ResourceNotFoundException;
// import com.tandapay.b2c.b2c_api.model.B2CRequest;
// import com.tandapay.b2c.b2c_api.repository.B2CRequestRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.util.Optional;

// @Service
// @RequiredArgsConstructor
// public class B2CService {
//     private final B2CRequestRepository b2cRequestRepository;
//     private final KafkaProducer kafkaProducer;

//     public B2CRequest processB2CRequest(B2CRequest request) {
//         if (request.getAmount() <= 0) {
//             throw new BadRequestException("Amount must be greater than zero.");
//         }
//         // Additional validation logic here

//         request.setStatus("Pending");
//         B2CRequest savedRequest = b2cRequestRepository.save(request);
//         kafkaProducer.sendMessage("b2c-request", savedRequest);
//         return savedRequest;
//     }

//     public B2CRequest getB2CRequestStatus(String id) {
//         return b2cRequestRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + id));
//     }

//     public B2CRequest updateB2CRequestStatus(String id, String status) {
//         Optional<B2CRequest> optionalRequest = b2cRequestRepository.findById(id);
//         if (optionalRequest.isPresent()) {
//             B2CRequest request = optionalRequest.get();
//             request.setStatus(status);
//             return b2cRequestRepository.save(request);
//         } else {
//             throw new ResourceNotFoundException("Request not found with id: " + id);
//         }
//     }
// }
