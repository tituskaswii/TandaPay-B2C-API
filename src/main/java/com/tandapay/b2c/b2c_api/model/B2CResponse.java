package com.tandapay.b2c.b2c_api.model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class B2CResponse {
    @Id
    private String conversationID;
    private String originatorConversationID;
    private String responseCode;
    private String responseDescription;

    private String requestId;
    private String status;
    // Add other fields as needed
}
