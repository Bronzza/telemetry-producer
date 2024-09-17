package com.example.producer.service;

import com.example.producer.config.DeviceMetadata;
import com.example.producer.config.KafkaConfig;
import com.example.producer.config.RouteDataProvider;
import com.example.producer.util.LocationCoordinatesCalculator;
import model.Location;
import model.TelemetryMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.producer.config.AppConfigTest.*;
import static com.example.producer.config.KafkaConfigTest.TEST_DEFAULT_TOPIC;
import static com.example.producer.util.SphericalInterpolatorTest.TEST_DISTANCE_KM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class TelemetryMessageProducerTest {
    public static final String TEST_LATITUDE = "TEST_LATIDUTE_1";
    public static final String TEST_LONGITUDE = "TEST_LONGITUDE_1";
    public static final String TEST_LATIDUTE_2 = "TEST_LATIDUTE_2";
    public static final String TEST_LONGITUDE_2 = "TEST_LONGITUDE_2";
    @Mock
    private KafkaTemplate<String, TelemetryMessage> kafkaTemplate;

    @InjectMocks
    private TelemetryMessageProducer telemetryMessageProducer;

    @Mock
    private DeviceMetadata deviceMetadata;
    @Mock
    private RouteDataProvider routeDataProvider;
    @Mock
    private LocationCoordinatesCalculator locationCalculator;
    @Mock
    private KafkaConfig kafkaConfig;
    @Captor
    ArgumentCaptor<TelemetryMessage> messageCaptor;
    @Captor
    ArgumentCaptor<Double> captor;


    @Test
    void sendMessage_shouldSendTelemetryMessage() throws InterruptedException {
        given(deviceMetadata.getDeviceId()).willReturn(TEST_DEFAULT_DEVICE_ID);
        given(deviceMetadata.getDeviceName()).willReturn(TEST_DEFAULT_DEVICE_NAME);
        given(routeDataProvider.getStartLatitude()).willReturn(TEST_DEFAULT_START_LATIDUTE);
        given(routeDataProvider.getStartLongitude()).willReturn(TEST_DEFAULT_START_LONGITUDE);
        given(routeDataProvider.getEndLatitude()).willReturn(TEST_DEFAULT_END_LATIDUTE);
        given(routeDataProvider.getEndLongitude()).willReturn(TEST_DEFAULT_END_LONGITUDE);
        given(routeDataProvider.getSpeed()).willReturn(TEST_DEFAULT_SPEED);
        given(kafkaConfig.getTopic()).willReturn(TEST_DEFAULT_TOPIC);

        given(locationCalculator.calculateDistance(
                eq(TEST_DEFAULT_START_LATIDUTE),
                eq(TEST_DEFAULT_START_LONGITUDE),
                eq(TEST_DEFAULT_END_LATIDUTE),
                eq(TEST_DEFAULT_END_LONGITUDE)))
                .willReturn(Double.valueOf(TEST_DISTANCE_KM));
        given(locationCalculator.calculateFraction(
                eq(Double.valueOf(TEST_DISTANCE_KM)),
                eq(TEST_DEFAULT_SPEED),
                anyInt()))
                .willReturn(0.5);
        given(locationCalculator.determineLocationCoordinates(
                eq(TEST_DEFAULT_START_LATIDUTE),
                eq(TEST_DEFAULT_START_LONGITUDE),
                eq(TEST_DEFAULT_END_LATIDUTE),
                eq(TEST_DEFAULT_END_LONGITUDE),
                captor.capture())).willAnswer(invocation -> {
            Location result = new Location();
            Double argument = invocation.getArgument(4);
            if (0.0d == Math.round(argument * 10) / 10d) {
                result.setLatitude(TEST_LATITUDE);
                result.setLongitude(TEST_LONGITUDE);
            } else if (0.5d == Math.round(argument * 10) / 10d) {
                result.setLatitude(TEST_LATIDUTE_2);
                result.setLongitude(TEST_LONGITUDE_2);
            }
            return result;
        });

        telemetryMessageProducer.sendMessage();

//        Left line commented, totaly valid line, but I prefer behavioral pattern (leave it as an valid example)
//        verify(kafkaTemplate, times(1)).send(eq(kafkaConfig.getTopic()), anyString(), messageCaptor.capture());
        then(kafkaTemplate).should(times(1)).send(eq(kafkaConfig.getTopic()), anyString(), messageCaptor.capture());

        List<TelemetryMessage> allValues = messageCaptor.getAllValues();
        TelemetryMessage firstMessage = allValues.get(0);
        assertEquals(LocalDateTime.now().getDayOfMonth(), firstMessage.getTimestamp().getDayOfMonth());
        assertEquals(LocalDateTime.now().getHour(), firstMessage.getTimestamp().getHour());
        assertEquals(TEST_LATITUDE, firstMessage.getLocation().getLatitude());
        assertEquals(TEST_LONGITUDE, firstMessage.getLocation().getLongitude());

    }
}