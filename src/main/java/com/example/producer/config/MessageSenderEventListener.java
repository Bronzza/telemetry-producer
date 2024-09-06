package com.example.producer.config;

import com.example.producer.service.TelemetryMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSenderEventListener {

    private final TelemetryMessageProducer producer;
    private final Environment environment;

    @EventListener(ApplicationReadyEvent.class)
    public void triggerOnStartup() throws InterruptedException {
        // Check if the 'test' or 'load-test' profile is active
        if (!isTestOrLoadTestProfileActive()) {
            producer.sendMessage();
        } else {
            log.info("Skipping sendMessage as test or load-test profile is active.");
        }
    }

    private boolean isTestOrLoadTestProfileActive() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(profile -> profile.equalsIgnoreCase("test") ||
                        profile.equalsIgnoreCase("load-test"));
    }
}
