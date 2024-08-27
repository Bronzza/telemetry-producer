package com.example.producer.service;

import com.example.producer.model.TelemetryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.producer.config.KafkaConfig.TELEMETRY_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetryMessageProducer {

    private final KafkaTemplate<String, TelemetryMessage> kafkaTemplate;

    private static final String TOPIC = "device_data";

    @Scheduled(fixedRate = 15000)
    public void sendMessage() {
        TelemetryMessage message = new TelemetryMessage(
                LocalDateTime.now(),
                new TelemetryMessage.Location(generateRandomLatitude(), generateRandomLongitude()),
                UUID.randomUUID().toString()
        );

        kafkaTemplate.send(TELEMETRY_TOPIC, message.getDeviceId(), message);
        System.out.println("Message sent: " + message);
    }

    private double generateRandomLatitude() {
        return -90 + Math.random() * 180;
    }

    private double generateRandomLongitude() {
        return -180 + Math.random() * 360;
    }
}
