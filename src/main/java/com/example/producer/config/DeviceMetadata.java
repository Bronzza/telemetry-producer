package com.example.producer.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class DeviceMetadata {
    private String deviceId;
    private String deviceName;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
