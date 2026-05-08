package com.ironhack.render_example.service.interfaces;

import com.ironhack.render_example.model.Instrument;

import java.util.List;

public interface InstrumentService {

    List<Instrument> findAll();
    Instrument findById(Long id);
    Instrument save(Instrument instrument);
    Instrument update(Long id, Instrument instrument);
    void delete(Long id);
}
