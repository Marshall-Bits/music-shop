package com.ironhack.render_example.service.impl;

import com.ironhack.render_example.model.Instrument;
import com.ironhack.render_example.repository.InstrumentRepository;
import com.ironhack.render_example.service.interfaces.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Override
    public List<Instrument> findAll() {
        return instrumentRepository.findAll();
    }

    @Override
    public Instrument findById(Long id) {
        return instrumentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instrument not found"));
    }

    @Override
    public Instrument save(Instrument instrument) {
        return instrumentRepository.save(instrument);
    }

    @Override
    public Instrument update(Long id, Instrument instrument) {
        if (!instrumentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Instrument not found");
        }
        instrument.setId(id);
        return instrumentRepository.save(instrument);
    }

    @Override
    public void delete(Long id) {
        if (!instrumentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Instrument not found");
        }
        instrumentRepository.deleteById(id);
    }
}
