package com.ironhack.render_example.controller;

import com.ironhack.render_example.model.Instrument;
import com.ironhack.render_example.service.interfaces.InstrumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InstrumentControllerTest {

    @Mock
    private InstrumentService instrumentService;

    @InjectMocks
    private InstrumentController instrumentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(instrumentController).build();
    }

    @Test
    void shouldFindAllInstrumentsSuccessfully() throws Exception {
        // Arrange
        Instrument guitar = new Instrument(1L, "Guitar", "Fender", "String", 599.99);
        Instrument piano = new Instrument(2L, "Piano", "Yamaha", "Keyboard", 1299.99);
        when(instrumentService.findAll()).thenReturn(List.of(guitar, piano));

        // Act & Assert
        mockMvc.perform(get("/api/instruments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Guitar"))
                .andExpect(jsonPath("$[0].brand").value("Fender"))
                .andExpect(jsonPath("$[0].type").value("String"))
                .andExpect(jsonPath("$[0].price").value(599.99))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Piano"))
                .andExpect(jsonPath("$[1].brand").value("Yamaha"));
    }

    @Test
    void shouldReturnEmptyListWhenNoInstruments() throws Exception {
        when(instrumentService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/instruments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldFindInstrumentByIdSuccessfully() throws Exception {
        // Arrange
        Instrument guitar = new Instrument(1L, "Guitar", "Fender", "String", 599.99);
        when(instrumentService.findById(1L)).thenReturn(guitar);

        // Act & Assert
        mockMvc.perform(get("/api/instruments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Guitar"))
                .andExpect(jsonPath("$.brand").value("Fender"))
                .andExpect(jsonPath("$.type").value("String"))
                .andExpect(jsonPath("$.price").value(599.99));
    }

    @Test
    void shouldReturn404WhenInstrumentNotFound() throws Exception {
        when(instrumentService.findById(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Instrument not found"));

        mockMvc.perform(get("/api/instruments/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSaveInstrumentSuccessfully() throws Exception {
        // Arrange
        Instrument saved = new Instrument(3L, "Drums", "Pearl", "Percussion", 899.99);
        when(instrumentService.save(any(Instrument.class))).thenReturn(saved);

        // Act & Assert
        mockMvc.perform(post("/api/instruments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Drums\",\"brand\":\"Pearl\",\"type\":\"Percussion\",\"price\":899.99}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Drums"))
                .andExpect(jsonPath("$.brand").value("Pearl"))
                .andExpect(jsonPath("$.type").value("Percussion"))
                .andExpect(jsonPath("$.price").value(899.99));
    }

    @Test
    void shouldUpdateInstrumentSuccessfully() throws Exception {
        // Arrange
        Instrument updated = new Instrument(1L, "Guitar", "Gibson", "String", 1200.00);
        when(instrumentService.update(eq(1L), any(Instrument.class))).thenReturn(updated);

        // Act & Assert
        mockMvc.perform(put("/api/instruments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Guitar\",\"brand\":\"Gibson\",\"type\":\"String\",\"price\":1200.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Guitar"))
                .andExpect(jsonPath("$.brand").value("Gibson"))
                .andExpect(jsonPath("$.type").value("String"))
                .andExpect(jsonPath("$.price").value(1200.00));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentInstrument() throws Exception {
        when(instrumentService.update(eq(99L), any(Instrument.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Instrument not found"));

        mockMvc.perform(put("/api/instruments/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Guitar\",\"brand\":\"Gibson\",\"type\":\"String\",\"price\":1200.00}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteInstrumentSuccessfully() throws Exception {
        doNothing().when(instrumentService).delete(1L);

        mockMvc.perform(delete("/api/instruments/1"))
                .andExpect(status().isNoContent());

        verify(instrumentService, times(1)).delete(1L);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentInstrument() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Instrument not found"))
                .when(instrumentService).delete(99L);

        mockMvc.perform(delete("/api/instruments/99"))
                .andExpect(status().isNotFound());
    }
}
