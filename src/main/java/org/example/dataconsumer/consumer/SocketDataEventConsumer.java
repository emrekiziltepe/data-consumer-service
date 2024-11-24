package org.example.dataconsumer.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocketDataEventConsumer implements EventConsumer<String> {

    private final ApplicationEventPublisher publisher;

    @Override
    @KafkaListener(
            topics = "${spring.kafka.template.default-topic}",
            autoStartup = "${spring.kafka.listener.auto-startup}",
            concurrency = "${spring.kafka.listener.concurrency}"
    )
    public void consume(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
                        @Header(KafkaHeaders.OFFSET) String offset,
                        @Payload String event) {
        try {
            publisher.publishEvent(event);
        } catch (Exception e) {
            throw new KafkaException(
                    String.format(
                            "Exception occurred at SocketDataEventConsumer data: %s Ex: %s, "
                                    + "KafkaLevelName: %s", event, e.getMessage(), KafkaException.Level.ERROR), e);
        }
    }
}
