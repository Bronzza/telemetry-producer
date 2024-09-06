package com.example.producer.util;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SphericalInterpolatorTest {

    public static final int DEGREE = 1;
    public static final double RADIANS = 0.0174533d;
    public static final double PRECISION = 10000000d;
    public static final int TEST_DISTANCE_KM = 100;
    public static final int TEST_SPEED_KM_SEC = 10;
    public static final int TEST_INTERVAL_MILLISEC = 10000;


    private LocationCoordinatesCalculator underTest = new LocationCoordinatesCalculator();

    @Test
    void toRadians() {
        double radians = underTest.toRadians(DEGREE);
        assertEquals(RADIANS, setPrecision(radians));
    }

    @Test
    void toDegrees() {
        double degrees = underTest.toDegrees(RADIANS);
        assertEquals(DEGREE, Math.round(degrees));
    }

    @Test
    void calculateDistance() {
// given coordinates represent to point A and B with distance 1km between them
        double distance = underTest.calculateDistance(50.0, 30.0, 50.008983, 30.0);
        assertEquals(1, Math.round(distance));
    }

    @Test
    void calculateFraction() {
        double fraction = underTest.calculateFraction(TEST_DISTANCE_KM, TEST_SPEED_KM_SEC, TEST_INTERVAL_MILLISEC);
        assertEquals(1, fraction);
    }

    private static double setPrecision(double radians) {
        return (double) Math.round(radians * PRECISION) / PRECISION;
    }
}