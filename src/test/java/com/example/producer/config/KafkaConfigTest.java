
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
public class KafkaConfigTest {

    public static final String TEST_DEFAULT_TOPIC = "TEST_TOPIC";
    public static final String TEST_BOOTSTRAP_SERVER = "localhost:9092";

    private final KafkaConfig kafkaConfig = new KafkaConfig();

    @BeforeEach
    void setup() {
        kafkaConfig.setBootstrapServers(TEST_BOOTSTRAP_SERVER);
        kafkaConfig.setTelemetry_topic(TEST_DEFAULT_TOPIC);
    }

    @Test
    void producerFactory_shouldReturnProducerFactory() {
        ProducerFactory<String, TelemetryMessage> producerFactory = kafkaConfig.producerFactory();
        assertNotNull(producerFactory);

        Map<String, Object> configProps = producerFactory.getConfigurationProperties();
        assertEquals(TEST_BOOTSTRAP_SERVER, configProps.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(JsonSerializer.class, configProps.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
    }

    @Test
    void kafkaTemplate_shouldReturnKafkaTemplate() {
        KafkaTemplate<String, TelemetryMessage> kafkaTemplate = kafkaConfig.kafkaTemplate();
        assertNotNull(kafkaTemplate);
    }
}