package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryMessage {

    private LocalDateTime timestamp;
    private Location location;
    private String deviceId;
    private String deviceName;
}
