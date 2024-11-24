package org.example.dataconsumer.service;

import lombok.RequiredArgsConstructor;
import org.example.dataconsumer.domain.entity.Data;
import org.example.dataconsumer.domain.repository.DataRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;

    public void save(Data data) {
        dataRepository.save(data);
    }
}
