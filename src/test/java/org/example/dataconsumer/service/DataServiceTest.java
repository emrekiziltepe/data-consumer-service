package org.example.dataconsumer.service;

import org.example.dataconsumer.domain.entity.Data;
import org.example.dataconsumer.domain.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DataServiceTest {

    @InjectMocks
    private DataService dataService;

    @Mock
    private DataRepository dataRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_validData() {
        //given
        var data = Data.builder()
                .timestamp(1638230400000L)
                .value(95)
                .hash("abc123")
                .build();

        //when
        dataService.save(data);

        //then
        var dataCaptor = ArgumentCaptor.forClass(Data.class);
        verify(dataRepository, times(1)).save(dataCaptor.capture());
        var capturedData = dataCaptor.getValue();

        assertEquals(1638230400000L, capturedData.getTimestamp());
        assertEquals(95, capturedData.getValue());
        assertEquals("abc123", capturedData.getHash());
    }

    @Test
    void testSave_repositoryThrowsException() {
        //given
        var data = Data.builder()
                .timestamp(1638230400000L)
                .value(95)
                .hash("abc123")
                .build();

        doThrow(new RuntimeException("Database error")).when(dataRepository).save(data);

        //when & then
        var exception = assertThrows(RuntimeException.class, () -> dataService.save(data));
        assertEquals("Database error", exception.getMessage());

        verify(dataRepository, times(1)).save(data);
    }
}
