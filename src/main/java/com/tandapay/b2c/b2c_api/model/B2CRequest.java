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
    private String status;

    // Ensure there are setter methods if Lombok is not used
    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    // Similarly, other setter methods if not using Lombok
    public void setId(String id) {
        this.id = id;
    }

    public void setOriginatorConversationID(String originatorConversationID) {
        this.originatorConversationID = originatorConversationID;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public void setSecurityCredential(String securityCredential) {
        this.securityCredential = securityCredential;
    }

    public void setCommandID(String commandID) {
        this.commandID = commandID;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setQueueTimeoutURL(String queueTimeoutURL) {
        this.queueTimeoutURL = queueTimeoutURL;
    }

    public void setResultURL(String resultURL) {
        this.resultURL = resultURL;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOccasion(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOccasion'");
    }
}