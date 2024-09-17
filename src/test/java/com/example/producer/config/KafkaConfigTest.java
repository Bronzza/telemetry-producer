package com.example.producer.config;

import model.TelemetryMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class KafkaConfigTest {
    private final KafkaConfig kafkaConfig = new KafkaConfig();

    @Value("${kafka.bootstrap-servers:localhost:9092}") // Default to localhost for non-Docker environments
    private String bootstrapServers;

    @BeforeEach
    void setUp() {
        kafkaConfig.setBootstrapServers(bootstrapServers);
    }

    @Test
    void producerFactory_shouldReturnProducerFactory() {
        ProducerFactory<String, TelemetryMessage> producerFactory = kafkaConfig.producerFactory();
        assertNotNull(producerFactory);

        Map<String, Object> configProps = producerFactory.getConfigurationProperties();
        assertEquals("localhost:9092", configProps.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(JsonSerializer.class, configProps.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
    }

    @Test
    void kafkaTemplate_shouldReturnKafkaTemplate() {
        KafkaTemplate<String, TelemetryMessage> kafkaTemplate = kafkaConfig.kafkaTemplate();
        assertNotNull(kafkaTemplate);
    }
}