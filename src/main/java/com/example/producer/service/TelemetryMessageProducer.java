package com.example.producer.service;

import com.example.producer.config.DeviceMetadata;
import com.example.producer.config.RouteDataProvider;
//import com.example.producer.util.SphericalInterpolator;
import com.example.producer.util.LocationCoordinatesCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Location;
import model.TelemetryMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.producer.config.KafkaConfig.TELEMETRY_TOPIC;

@Slf4j
@Service
@Profile("!load-test")
@RequiredArgsConstructor
public class TelemetryMessageProducer {

    private final KafkaTemplate<String, TelemetryMessage> kafkaTemplate;
    private final DeviceMetadata deviceMetadata;
    private final RouteDataProvider routeDataProvider;
    private final LocationCoordinatesCalculator locationCalculator;

    @Value("${app.interval}")
    private int interval;

    public void sendMessage() throws InterruptedException {
        double distance = getDistance();
        double fraction = locationCalculator.calculateFraction(distance, routeDataProvider.getSpeed(), interval);

        for (double currentPartOfRoute = 0; currentPartOfRoute < 1; currentPartOfRoute += fraction) {
            TelemetryMessage message = createTelemetryMessage(currentPartOfRoute);

            kafkaTemplate.send(TELEMETRY_TOPIC, message.getDeviceId(), message);
            Thread.sleep(interval);
            log.info("Message send {}", message);
        }
    }

    private double getDistance() {
        return locationCalculator.calculateDistance(
                routeDataProvider.getStartLatitude(),
                routeDataProvider.getStartLongitude(),
                routeDataProvider.getEndLatitude(),
                routeDataProvider.getEndLongitude());
    }

    private TelemetryMessage createTelemetryMessage(double currentFraction) {
        TelemetryMessage message = new TelemetryMessage(
                LocalDateTime.now(),
                getCurrentLocation(Double.valueOf(currentFraction)),
                deviceMetadata.getDeviceId(), deviceMetadata.getDeviceName()
        );
        return message;
    }

    private Location getCurrentLocation(Double fraction) {
        return locationCalculator.determineLocationCoordinates(routeDataProvider.getStartLatitude(),
                routeDataProvider.getStartLongitude(),
                routeDataProvider.getEndLatitude(),
                routeDataProvider.getEndLongitude(), fraction);
    }
}
