package org.example.dataconsumer.consumer;

@FunctionalInterface
public interface EventConsumer<T> {

    void consume(String topic, String partition, String offset, T message);
}
