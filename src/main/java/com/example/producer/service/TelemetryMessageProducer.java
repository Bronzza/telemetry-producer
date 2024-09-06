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
import org.springframework.context.annotation.Profile;
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

    private double currentPartOfRoute = 0.0;  // Track the current step of the route
    private boolean routeCompleted = false;   // Flag to stop sending after route completion


    @Value("${app.interval}")
    private int interval;

    @Scheduled(fixedRateString = "${app.interval}")
    public void sendMessage() {
        if (routeCompleted) {
            log.info("Route is completed. No more messages to send.");
            return;
        }

        double distance = getDistance();
        double fraction = locationCalculator.calculateFraction(distance, routeDataProvider.getSpeed(), interval);

        TelemetryMessage message = createTelemetryMessage(currentPartOfRoute);
        kafkaTemplate.send(TELEMETRY_TOPIC, message.getDeviceId(), message);
        log.info("Message sent at route step {}: {}", currentPartOfRoute, message);

        currentPartOfRoute += fraction;

        if (currentPartOfRoute >= 1.0) {
            routeCompleted = true;
        }
    }

    private TelemetryMessage createTelemetryMessage(double currentPartOfRoute) {
        // Logic to create a telemetry message with the current position
        Location location = locationCalculator.determineLocationCoordinates(
                routeDataProvider.getStartLatitude(), routeDataProvider.getStartLongitude(),
                routeDataProvider.getEndLatitude(), routeDataProvider.getEndLongitude(),
                currentPartOfRoute
        );
        return createMessageApplyLocation(location);
    }

    private double getDistance() {
        return locationCalculator.calculateDistance(
                routeDataProvider.getStartLatitude(),
                routeDataProvider.getStartLongitude(),
                routeDataProvider.getEndLatitude(),
                routeDataProvider.getEndLongitude());
    }

    private TelemetryMessage createMessageApplyLocation(Location location) {
        TelemetryMessage message = new TelemetryMessage(
                LocalDateTime.now(),
                location,
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
