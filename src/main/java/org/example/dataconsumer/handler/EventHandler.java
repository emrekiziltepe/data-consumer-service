package org.example.dataconsumer.handler;

import org.springframework.context.event.EventListener;

public interface EventHandler<T> {

    @EventListener
    void handle(T event);
}
