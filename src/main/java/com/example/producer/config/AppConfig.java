package com.example.producer.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    public static final String DEVICE_ID_ARG = "app.deviceId";
    public static final String DEVICE_NAME_ARG = "app.deviceName";
    public static final String START_LAT_ROUTE = "app.startLatitude";
    public static final String START_LONG_ROUTE = "app.startLongitude";
    public static final String END_LAT_ROUTE = "app.endLatitude";
    public static final String END_LONG_ROUTE = "app.endLongitude";
    public static final String SPEED_ROUTE = "app.speed";
    public static final String INTERVAL_ROUTE = "app.interval";

    private String deviceId;
    private String deviceName;
    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;
    private Double speed;
    private Integer interval;

    @Bean
    public DeviceMetadata deviceMetadata() {
        return new DeviceMetadata(deviceId, deviceName);
    }

    @Bean
    public RouteDataProvider routeDataProvider() {
        return RouteDataProvider.builder()
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
                .endLatitude(endLatitude)
                .endLongitude(endLongitude)
                .speed(speed)
                .interval(interval)
                .build();
    }

}
