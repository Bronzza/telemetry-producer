package com.example.producer;

import lombok.RequiredArgsConstructor;
import model.Location;
import model.TelemetryMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Component
@Profile("load-test")
@RequiredArgsConstructor
public class LoadTestingRunner implements CommandLineRunner {

    private final KafkaTemplate<String, TelemetryMessage> kafkaTemplate;
    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopic;

    @Override
    public void run(String... args) throws Exception {
        int messageCount = 100000;
        int partitionCount = 10; // it should correspond with command in info.txt for appropriate topic

        for (int i = 0; i < messageCount; i++) {
            TelemetryMessage message = generateTelemetryMessage();
            int partition = new Random().nextInt(partitionCount);  // Distribute messages across partitions
            kafkaTemplate.send(kafkaTopic, partition, message.getDeviceId(), message);
            if (i % 1000 == 0) {
                System.out.println("Sent " + i + " messages");
            }
        }
        System.out.println("Load testing completed. Sent " + messageCount + " messages.");
    }

    private TelemetryMessage generateTelemetryMessage() {
        String deviceId = UUID.randomUUID().toString();
        String deviceName = "Drone-" + new Random().nextInt(1000);
        LocalDateTime timestamp = LocalDateTime.now();
        Location location = new Location(generateRandomCoordinate(), generateRandomCoordinate());

        return new TelemetryMessage(timestamp, location, deviceId, deviceName);
    }

    private String generateRandomCoordinate() {
        return String.format("%.6f", new Random().nextDouble() * 180 - 90);
    }
}
