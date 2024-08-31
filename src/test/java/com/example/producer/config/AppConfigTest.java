package com.example.producer.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AppConfigTest {
    public static final String TEST_DEFAULT_DEVICE_ID = "testDefaultDeviceId";
    public static final String TEST_DEFAULT_DEVICE_NAME = "testDefaultDeviceName";
    @Autowired
    private DeviceMetadata deviceMetadata;

    @Test
    public void testProfileValues() {
        assertEquals(TEST_DEFAULT_DEVICE_ID, deviceMetadata.getDeviceId());
        assertEquals(TEST_DEFAULT_DEVICE_NAME, deviceMetadata.getDeviceName());
    }
}