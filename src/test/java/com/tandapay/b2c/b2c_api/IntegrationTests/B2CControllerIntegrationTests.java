package com.tandapay.b2c.b2c_api.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tandapay.b2c.b2c_api.model.B2CRequest;
import com.tandapay.b2c.b2c_api.repository.B2CRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class B2CControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private B2CRequestRepository b2cRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        b2cRequestRepository.deleteAll();
    }

    @Test
    public void createB2CRequestTest() throws Exception {
        B2CRequest request = new B2CRequest();
        request.setOriginatorConversationID("feb5e3f2-fbbc-4745-844c-ee37b546f627");
        request.setInitiatorName("testapi");
        request.setSecurityCredential("EsJocK7+NjqZPC3I3EO+TbvS+xVb9TymWwaKABoaZr/Z/n0UysSs..");
        request.setCommandID("BusinessPayment");
        request.setAmount(10.0);
        request.setPartyA("600996");
        request.setPartyB("254728762287");
        request.setRemarks("here are my remarks");
        request.setQueueTimeoutURL("https://mydomain.com/b2c/queue");
        request.setResultURL("https://mydomain.com/b2c/result");
        request.setOccasion("Christmas");

        mockMvc.perform(post("/api/b2c/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originatorConversationID").value("feb5e3f2-fbbc-4745-844c-ee37b546f627"))
                .andExpect(jsonPath("$.status").value("Pending"));
    }

    @Test
    public void getB2CRequestStatusTest() throws Exception {
        B2CRequest request = new B2CRequest();
        request.setId("1");
        request.setOriginatorConversationID("feb5e3f2-fbbc-4745-844c-ee37b546f627");
        request.setStatus("Pending");
        b2cRequestRepository.save(request);

        mockMvc.perform(get("/api/b2c/status/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originatorConversationID").value("feb5e3f2-fbbc-4745-844c-ee37b546f627"))
                .andExpect(jsonPath("$.status").value("Pending"));
    }

    @Test
    public void updateB2CRequestStatusTest() throws Exception {
        B2CRequest request = new B2CRequest();
        request.setId("1");
        request.setStatus("Pending");
        b2cRequestRepository.save(request);

        mockMvc.perform(put("/api/b2c/status/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Completed"));
    }

    @Test
    public void getB2CRequestStatusNotFoundTest() throws Exception {
        mockMvc.perform(get("/api/b2c/status/1"))
                .andExpect(status().isNotFound());
    }
}
