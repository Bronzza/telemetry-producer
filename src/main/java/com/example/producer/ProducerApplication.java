package com.example.producer;

import com.example.producer.config.DeviceMetadata;
import com.example.producer.config.RouteDataProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.producer.config.AppConfig.*;

@SpringBootApplication
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(DeviceMetadata deviceMetadata, RouteDataProvider dataProvider) {
		return args -> {
			setMetadata(deviceMetadata, dataProvider, args);
		};
	}

	private static void setMetadata(DeviceMetadata deviceMetadata, RouteDataProvider dataProvider, String[] args) {
		for (String arg : args) {
			String[] keyValue = arg.split("=");
			if (keyValue.length == 2) {
				switch (keyValue[0]) {
					case DEVICE_ID_ARG:
						deviceMetadata.setDeviceId(keyValue[1]);
						break;
					case DEVICE_NAME_ARG:
						deviceMetadata.setDeviceName(keyValue[1]);
						break;
					case START_LAT_ROUTE:
						dataProvider.setStartLatitude(Double.valueOf(keyValue[1]));
						break;
					case START_LONG_ROUTE:
						dataProvider.setStartLongitude(Double.valueOf(keyValue[1]));
						break;
					case END_LAT_ROUTE:
						dataProvider.setEndLatitude(Double.valueOf(keyValue[1]));
						break;
					case END_LONG_ROUTE:
						dataProvider.setEndLongitude(Double.valueOf(keyValue[1]));
						break;
					case SPEED_ROUTE:
						dataProvider.setSpeed(Double.valueOf(keyValue[1]));
						break;
				}
			}
		}
	}

}
