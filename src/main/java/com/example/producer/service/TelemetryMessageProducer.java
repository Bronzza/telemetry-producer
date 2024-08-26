package com.example.producer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class TelemetryMessageProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "device_data";

    @Scheduled(fixedRate = 10000)
    public void sendMessage() {
        System.out.println("Here");
        Map<String, Object> message = new HashMap<>();
        message.put("timestamp", LocalDateTime.now().toString());
        message.put("location", "MockLocation");
        message.put("deviceId", UUID.randomUUID().toString());
        message.put("name", "DeviceName");

        kafkaTemplate.send(TOPIC, message);
        System.out.println("Message sent: " + message);
        log.debug("Message sent: {}", message);
    }
}
