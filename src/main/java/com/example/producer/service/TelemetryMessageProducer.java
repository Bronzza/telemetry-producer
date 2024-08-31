package com.example.producer.service;

import com.example.producer.config.DeviceMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Location;
import model.TelemetryMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.producer.config.KafkaConfig.TELEMETRY_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetryMessageProducer {

    private final KafkaTemplate<String, TelemetryMessage> kafkaTemplate;
    private final DeviceMetadata deviceMetadata;

    @Scheduled(fixedRate = 15000)
    public void sendMessage() {
        TelemetryMessage message = new TelemetryMessage(
                LocalDateTime.now(),
                new Location(generateRandomLatitude(), generateRandomLongitude()),
                deviceMetadata.getDeviceId(), deviceMetadata.getDeviceName()
        );

        kafkaTemplate.send(TELEMETRY_TOPIC, message.getDeviceId(), message);
        System.out.println("Message sent: " + message);
    }

    private String generateRandomLatitude() {
        return String.valueOf(-90 + Math.random() * 180);
    }

    private String generateRandomLongitude() {
        return String.valueOf(-180 + Math.random() * 360);
    }
}
