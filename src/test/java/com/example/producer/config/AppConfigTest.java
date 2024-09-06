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
    public static final Double TEST_DEFAULT_START_LATIDUTE = 50.0;
    public static final Double TEST_DEFAULT_START_LONGITUDE = 50.0;
    public static final Double TEST_DEFAULT_END_LATIDUTE = 51.0;
    public static final Double TEST_DEFAULT_END_LONGITUDE = 51.0;
    public static final Double TEST_DEFAULT_SPEED = 0.1;
    public static final Integer TEST_DEFAULT_INTERVAL = 10000;

    @Autowired
    private DeviceMetadata deviceMetadata;

    @Autowired
    private RouteDataProvider dataProvider;

    @Test
    public void testMetadataPropertiesValues() {
        assertEquals(TEST_DEFAULT_DEVICE_ID, deviceMetadata.getDeviceId());
        assertEquals(TEST_DEFAULT_DEVICE_NAME, deviceMetadata.getDeviceName());
    }

    @Test
    public void testRoadDataPropertiesValues() {
        assertEquals(TEST_DEFAULT_START_LATIDUTE, dataProvider.getStartLatitude());
        assertEquals(TEST_DEFAULT_START_LONGITUDE, dataProvider.getStartLongitude());
        assertEquals(TEST_DEFAULT_END_LATIDUTE, dataProvider.getEndLatitude());
        assertEquals(TEST_DEFAULT_END_LONGITUDE, dataProvider.getEndLongitude());
        assertEquals(TEST_DEFAULT_SPEED, dataProvider.getSpeed());
        assertEquals(TEST_DEFAULT_INTERVAL, dataProvider.getInterval());
    }
}