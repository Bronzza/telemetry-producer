package com.example.producer.service;

import com.example.producer.config.DeviceMetadata;
import com.example.producer.config.KafkaConfig;
import model.TelemetryMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

import static com.example.producer.config.AppConfigTest.TEST_DEFAULT_DEVICE_ID;
import static com.example.producer.config.AppConfigTest.TEST_DEFAULT_DEVICE_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelemetryMessageProducerTest {
    @Mock
    private KafkaTemplate<String, TelemetryMessage> kafkaTemplate;

    @InjectMocks
    private TelemetryMessageProducer telemetryMessageProducer;

    @Mock
    private DeviceMetadata deviceMetadata;

    @Captor
    ArgumentCaptor<TelemetryMessage> messageCaptor;

    @Test
    void sendMessage_shouldSendTelemetryMessage() {
        given(deviceMetadata.getDeviceId()).willReturn(TEST_DEFAULT_DEVICE_ID);
        given(deviceMetadata.getDeviceName()).willReturn(TEST_DEFAULT_DEVICE_NAME);
        telemetryMessageProducer.sendMessage();

//        Left line commented, totaly valid line, but I prefer behavioral pattern (leave it as an valid example)
//        verify(kafkaTemplate, times(1)).send(eq(KafkaConfig.TELEMETRY_TOPIC), anyString(), messageCaptor.capture());
        then(kafkaTemplate).should(times(1)).send(eq(KafkaConfig.TELEMETRY_TOPIC), anyString(), messageCaptor.capture());

        TelemetryMessage capturedMessage = messageCaptor.getValue();
        assertEquals(LocalDateTime.now().getDayOfMonth(), capturedMessage.getTimestamp().getDayOfMonth());
        assertEquals(LocalDateTime.now().getHour(), capturedMessage.getTimestamp().getHour());
    }
}