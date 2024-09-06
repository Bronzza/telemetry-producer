package com.example.producer.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.producer.config.AppConfigTest.TEST_DEFAULT_DEVICE_ID;
import static com.example.producer.config.AppConfigTest.TEST_DEFAULT_DEVICE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class DeviceMetadataTest {
    @Autowired
    private DeviceMetadata deviceMetadata;

    @Test
    @Disabled("Duplicated logic with AppConfigTest")
    public void testDefaultValues() {
        assertEquals(TEST_DEFAULT_DEVICE_ID, deviceMetadata.getDeviceId());
        assertEquals(TEST_DEFAULT_DEVICE_NAME, deviceMetadata.getDeviceName());
    }
}