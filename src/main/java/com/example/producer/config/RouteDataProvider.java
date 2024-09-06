package com.example.producer.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Builder
@Data
@AllArgsConstructor
public class RouteDataProvider {

    //coordinates
    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;
    // km/sec
    private Double speed;
    //milliseconds
    private Integer interval;

}
