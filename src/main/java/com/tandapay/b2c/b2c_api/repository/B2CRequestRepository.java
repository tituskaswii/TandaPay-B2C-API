package com.tandapay.b2c.b2c_api.repository;

import com.tandapay.b2c.b2c_api.model.B2CRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface B2CRequestRepository extends MongoRepository<B2CRequest, String> {
}
