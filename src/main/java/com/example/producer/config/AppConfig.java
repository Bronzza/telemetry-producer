package com.example.producer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    public static final String DEVICE_ID_ARG = "app.deviceId";
    public static final String DEVICE_NAME_ARG = "app.deviceName";

    private String deviceId;
    private String deviceName;

    @Bean
    public DeviceMetadata deviceMetadata() {
        return new DeviceMetadata(deviceId, deviceName);
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
