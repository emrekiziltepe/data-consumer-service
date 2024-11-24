package org.example.dataconsumer.handler;

import org.example.dataconsumer.domain.entity.Data;
import org.example.dataconsumer.service.DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class SocketDataEventHandlerTest {

    @InjectMocks
    private SocketDataEventHandler socketDataEventHandler;

    @Mock
    private DataService dataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle_validEvent() {
        //given
        var event = "1638230400000,95,12";

        //when
        socketDataEventHandler.handle(event);

        //then
        var dataCaptor = ArgumentCaptor.forClass(Data.class);
        verify(dataService, times(1)).save(dataCaptor.capture());
        var capturedData = dataCaptor.getValue();

        assertEquals(1638230400000L, capturedData.getTimestamp());
        assertEquals(95, capturedData.getValue());
        assertEquals("12", capturedData.getHash());
    }

    @Test
    void testHandle_invalidFormat_missingParts() {
        //given
        var event = "1638230400000,95";

        //when
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            socketDataEventHandler.handle(event);
        });

        //then
        assertTrue(exception.getMessage().contains("Index"));
        verifyNoInteractions(dataService);
    }

    @Test
    void testHandle_invalidFormat_nonNumericTimestamp() {
        //given
        var event = "invalid,95,abc123";

        //when
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            socketDataEventHandler.handle(event);
        });

        //then
        assertTrue(exception.getMessage().contains("invalid"));
        verifyNoInteractions(dataService);
    }

    @Test
    void testHandle_invalidFormat_nonNumericValue() {
        //given
        var event = "1638230400000,invalid,abc123";

        //when
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            socketDataEventHandler.handle(event);
        });

        //then
        assertTrue(exception.getMessage().contains("invalid"));
        verifyNoInteractions(dataService);
    }

    @Test
    void testHandle_dataServiceThrowsException() {
        //given
        var event = "1638230400000,95,abc123";
        doThrow(new RuntimeException("Database error")).when(dataService).save(any(Data.class));

        //when
        Exception exception = assertThrows(RuntimeException.class, () -> {
            socketDataEventHandler.handle(event);
        });

        //then
        assertEquals("Database error", exception.getMessage());
        verify(dataService, times(1)).save(any(Data.class));
    }
}
