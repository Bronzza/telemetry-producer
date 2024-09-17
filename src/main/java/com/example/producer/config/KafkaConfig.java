package com.example.producer.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.TelemetryMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
@Setter
public class KafkaConfig {

    public final static String TELEMETRY_TOPIC = "device_data";

    @Value("${kafka.bootstrap-servers:localhost:9092}") // Default to localhost for non-Docker environments
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, TelemetryMessage> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers); // Use externalized broker URL
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        log.info("Kafka ProducerFactory configured with broker URL: {}", bootstrapServers);
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public KafkaTemplate<String, TelemetryMessage> kafkaTemplate() {
        System.out.println("Kafka template created");
        return new KafkaTemplate<>(producerFactory());
    }
}
