package com.example.producer.config;

import com.example.producer.model.TelemetryMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConfigTest {
    private final KafkaConfig kafkaConfig = new KafkaConfig();

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