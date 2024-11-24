package org.example.dataconsumer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataconsumer.domain.entity.Data;
import org.example.dataconsumer.service.DataService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SocketDataEventHandler implements EventHandler<String> {

    private final DataService dataService;

    public void handle(String event) {
        var parts = event.split(",");
        var data = Data.builder()
                .timestamp(Long.parseLong(parts[0]))
                .value(Integer.parseInt(parts[1]))
                .hash(parts[2])
                .build();
        dataService.save(data);
    }

}
