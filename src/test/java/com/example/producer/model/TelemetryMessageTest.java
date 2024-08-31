package com.example.producer.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.TelemetryMessage;
import org.junit.jupiter.api.Test;

class TelemetryMessageTest {

    public static final String MESSAGE = "{\"timestamp\":\"2024-08-29T14:45:30\",\"location\":{\"latitude\":12.34,\"longitude\":56.78},\"deviceId\":\"device-123\"}";

    @Test
    public void testDeserialization() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            TelemetryMessage message = objectMapper.readValue(MESSAGE, TelemetryMessage.class);
            System.out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}