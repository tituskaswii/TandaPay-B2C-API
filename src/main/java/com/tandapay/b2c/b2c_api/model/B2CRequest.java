package com.tandapay.b2c.b2c_api.model;

import com.tandapay.b2c.b2c_api.validation.PartyBValidator;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "b2c_requests")
public class B2CRequest {
    @Id
    private String id;

    @NotBlank(message = "OriginatorConversationID is required")
    private String originatorConversationID;

    @NotBlank(message = "InitiatorName is required")
    private String initiatorName;

    @NotBlank(message = "SecurityCredential is required")
    private String securityCredential;

    @NotBlank(message = "CommandID is required")
    private String commandID;

    @Min(value = 1, message = "Amount must be greater than zero")
    private double amount;

    @NotBlank(message = "PartyA is required")
    private String partyA;

    @PartyBValidator
    private String partyB;

    @NotBlank(message = "Remarks are required")
    private String remarks;

    @NotBlank(message = "QueueTimeOutURL is required")
    private String queueTimeoutURL;

    @NotBlank(message = "ResultURL is required")
    private String resultURL;

    private String occasion;
    private String status;
}
