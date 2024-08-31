package com.example.producer.util;

import org.apache.kafka.common.utils.Time;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateTimeUtil {

    public static Instant dateTimeToInstant(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return Instant.ofEpochSecond(instant.getEpochSecond()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
