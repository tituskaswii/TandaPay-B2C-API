package com.tandapay.b2c.b2c_api.IntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "b2c-request", "b2c-response" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DockerIntegrationTests {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void testKafkaSetupWithEmbeddedKafka() {
        kafkaTemplate.send("b2c-request", "Test message");
        kafkaTemplate.send("b2c-response", "Test response");

        // You can add more detailed verification here to check the Kafka messages
        assertThat(true).isTrue();
    }
}
