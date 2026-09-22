package ru.ivanov.queues;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

@Testcontainers
public class IdempotencyTest {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");
    @Container
    private static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));
    private static final String TOPIC = "orders";
    private static DbManager dbManager;

    @BeforeAll
    static void beforeAll() {
        dbManager = new DbManager(postgres);
    }

    @Test
    void testConsumerIdempotencyWithDuplicateMessages() {
        TestProducer producer = new TestProducer(kafka.getBootstrapServers());
        TestConsumer consumer = new TestConsumer(kafka.getBootstrapServers(), dbManager, TOPIC);

        Message duplicateEvent = new Message("123", "PAID");
        producer.send(TOPIC, duplicateEvent);
        producer.send(TOPIC, duplicateEvent);
        producer.send(TOPIC, duplicateEvent);

        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(200))
                .untilAsserted(() -> {
                            int numberOfOrders = dbManager.getNumberOfOrders();
                            Assertions.assertThat(numberOfOrders).isEqualTo(1);
                        }
                );
        producer.close();
        consumer.stop();
    }
}
