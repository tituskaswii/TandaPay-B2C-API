package com.tandapay.b2c.b2c_api.model;

import lombok.Data;

@Data
public class B2CResponse {
    private String conversationID;
    private String originatorConversationID;
    private String responseCode;
    private String responseDescription;
}
