package com.example.producer.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.KafkaContainer;
import org.junit.jupiter.api.Test;

import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class KafkaTest {

    public static final String TEST_TOPIC = "test_topic";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String DOCKER_IMAGE = "confluentinc/cp-kafka:7.4.1";
    public static final String TEST_GROUP = "test-group";
    public static final String AUTO_OFFSET_CONFIG_VALUE = "earliest";

    @Test
    public void testKafkaProducerAndConsumer() {
        // Start a Kafka container
        try (KafkaContainer kafka = new KafkaContainer(DockerImageName.parse(DOCKER_IMAGE))) {
            kafka.start();
            // Set up producer properties
            Properties producerProps = createProperties(kafka, true);

            // Create and send messages
            try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {
                producer.send(new ProducerRecord<>(TEST_TOPIC, KEY, VALUE));
            }
            Properties consumerProps = createProperties(kafka, false);

            // Create a Kafka consumer
            try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
                consumer.subscribe(Collections.singletonList(TEST_TOPIC));

                // Poll for messages
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));

                // Verify that the expected message was received
                assertThat(records.count()).isGreaterThan(0);
                for (ConsumerRecord<String, String> record : records) {
                    assertThat(record.key()).isEqualTo(KEY);
                    assertThat(record.value()).isEqualTo(VALUE);
                }
            }
        }
    }

    @NotNull
    private static Properties createProperties(KafkaContainer kafka, Boolean isProducer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        if (isProducer) {
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        } else {
            props.put(ConsumerConfig.GROUP_ID_CONFIG, TEST_GROUP);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_CONFIG_VALUE);
        }
        return props;
    }
}
