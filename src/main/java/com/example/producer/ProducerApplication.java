package com.example.producer;

import com.example.producer.config.DeviceMetadata;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.example.producer.config.AppConfig.DEVICE_ID_ARG;
import static com.example.producer.config.AppConfig.DEVICE_NAME_ARG;

@EnableScheduling
@SpringBootApplication
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(DeviceMetadata deviceMetadata) {
		return args -> {
			setDeviceMetadata(deviceMetadata, args);
		};
	}

	private static void setDeviceMetadata(DeviceMetadata deviceMetadata, String[] args) {
		for (String arg : args) {
			String[] keyValue = arg.split("=");
			if (keyValue.length == 2) {
				switch (keyValue[0]) {
					case DEVICE_ID_ARG:
						deviceMetadata.setDeviceName(keyValue[1]);
						break;
					case DEVICE_NAME_ARG:
						deviceMetadata.setDeviceName(keyValue[1]);
						break;
				}
			}
		}
	}

}
