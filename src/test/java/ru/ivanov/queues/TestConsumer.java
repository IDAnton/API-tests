package ru.ivanov.queues;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestConsumer {
    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DbManager dbManager;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public TestConsumer(String bootstrapServers, DbManager dbManager, String topic) {
        this.dbManager = dbManager;
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-processors");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        this.consumer = new KafkaConsumer<>(props);
        startConsumeLoop(topic);
    }

    public void startConsumeLoop(String topic) {
        new Thread(() -> {
            try {
                consumer.subscribe(Collections.singletonList(topic));
                while (running.get()) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(200));
                    if (!records.isEmpty()) {
                        List<Message> messages = new ArrayList<>();
                        for (ConsumerRecord<String, String> record : records) {
                            Message message = objectMapper.readValue(record.value(), Message.class);
                            messages.add(message);
                        }
                        dbManager.insertIntoOrders(messages);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    consumer.close();
                } catch (Exception e) {
                    System.err.println("Ошибка при закрытии консьюмера: " + e.getMessage());
                }
            }
        }, "kafka-test-consumer-thread").start();
    }

    public void stop() {
        running.set(false);
    }
}