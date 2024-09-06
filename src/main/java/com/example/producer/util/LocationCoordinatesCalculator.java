package com.example.producer.util;

import model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationCoordinatesCalculator {

    // Earth's radius in kilometers
    private static final double EARTH_RADIUS = 6371.0;
    public static final String INVALID_COORDANATE = "-1000000";
    private static final Location INVALID_LOCATION = new Location(INVALID_COORDANATE, INVALID_COORDANATE);

    // Convert degrees to radians
    protected double toRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    // Convert radians to degrees
    protected double toDegrees(double radians) {
        return radians * 180 / Math.PI;
    }

    // Haversine formula to calculate the great-circle distance between two points
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // returns the distance in kilometers
    }

    // Spherical linear interpolation (Slerp) to find intermediate points
    public Location determineLocationCoordinates(double lat1, double lon1, double lat2, double lon2, double fraction) {
        // Convert degrees to radians
        lat1 = toRadians(lat1);
        lon1 = toRadians(lon1);
        lat2 = toRadians(lat2);
        lon2 = toRadians(lon2);

        // Haversine formula for great-circle distance
        double d = calculateDistance(toDegrees(lat1), toDegrees(lon1), toDegrees(lat2), toDegrees(lon2));
        // Convert distance from kilometers to radians
        double dRad = d / EARTH_RADIUS;

        // If distance is very small, return the starting point
        if (dRad < 1e-10) {
            return new Location(String.valueOf(toDegrees(lat1)), String.valueOf(toDegrees(lon1)));
        }

        // Fractional interpolation using spherical trigonometry
        double a = Math.sin((1 - fraction) * dRad) / Math.sin(dRad);
        double b = Math.sin(fraction * dRad) / Math.sin(dRad);

        // Interpolated latitude and longitude
        double x = a * Math.cos(lat1) * Math.cos(lon1) + b * Math.cos(lat2) * Math.cos(lon2);
        double y = a * Math.cos(lat1) * Math.sin(lon1) + b * Math.cos(lat2) * Math.sin(lon2);
        double z = a * Math.sin(lat1) + b * Math.sin(lat2);

        double lat = Math.atan2(z, Math.sqrt(x * x + y * y));
        double lon = Math.atan2(y, x);

        // Convert back to degrees
        return new Location(String.valueOf(toDegrees(lat)), String.valueOf(toDegrees(lon)));
    }


    public double calculateFraction(double distance, double speed, int interval) {
        double timeInSeconds = (distance / speed);
        double fraction = (interval / 1000d) / timeInSeconds;
        return fraction;

    }


    public static void main(String[] args) {
//        double latA = 50.4501;
//        double lonA = 30.5234;
//
//        double latB = 55.7558;
//        double lonB = 37.6173;
//        double fraction = 0.07;
//        for (double i = 0; i < 1; i += fraction) {
//            System.out.println("i: " + i);
//            Location interpolate = interpolate(latA, lonA, latB, lonB, i);
//            System.out.println("lat: " + interpolate.getLatitude() + "long: " + interpolate.getLongitude());
//        }
    }
}
