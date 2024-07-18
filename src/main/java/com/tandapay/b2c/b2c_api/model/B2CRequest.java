package com.tandapay.b2c.b2c_api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "b2c_requests")
public class B2CRequest {
    @Id
    private String id;
    private String originatorConversationID;
    private String initiatorName;
    private String securityCredential;
    private String commandID;
    private double amount;
    private String partyA;
    private String partyB;
    private String remarks;
    private String queueTimeoutURL;
    private String resultURL;
    private String occasion;
    private String status;
}
