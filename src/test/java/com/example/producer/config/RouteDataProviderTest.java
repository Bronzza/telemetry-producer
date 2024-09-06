package com.example.producer.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import static com.example.producer.config.AppConfigTest.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RouteDataProviderTest {

    @Autowired
    private RouteDataProvider provider;

    @Test
    @Disabled("Duplicated logic with AppConfigTest")
    public void testRoadDataPropertiesValues() {
        assertEquals(TEST_DEFAULT_START_LATIDUTE, provider.getStartLatitude());
        assertEquals(TEST_DEFAULT_START_LONGITUDE, provider.getStartLongitude());
        assertEquals(TEST_DEFAULT_END_LATIDUTE, provider.getEndLatitude());
        assertEquals(TEST_DEFAULT_END_LONGITUDE, provider.getEndLongitude());
        assertEquals(TEST_DEFAULT_SPEED, provider.getSpeed());
        assertEquals(TEST_DEFAULT_INTERVAL, provider.getInterval());
    }

}