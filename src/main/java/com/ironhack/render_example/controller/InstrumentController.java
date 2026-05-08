package com.ironhack.render_example.controller;

import com.ironhack.render_example.model.Instrument;
import com.ironhack.render_example.service.interfaces.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

    @Autowired
    private InstrumentService instrumentService;

    @GetMapping
    public List<Instrument> findAll() {
        return instrumentService.findAll();
    }

    @GetMapping("/{id}")
    public Instrument findById(@PathVariable Long id) {
        return instrumentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Instrument save(@RequestBody Instrument instrument) {
        return instrumentService.save(instrument);
    }

    @PutMapping("/{id}")
    public Instrument update(@PathVariable Long id, @RequestBody Instrument instrument) {
        return instrumentService.update(id, instrument);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        instrumentService.delete(id);
    }
}
