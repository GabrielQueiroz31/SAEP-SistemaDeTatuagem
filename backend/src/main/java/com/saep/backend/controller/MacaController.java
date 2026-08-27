package com.saep.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saep.backend.Repository.MacaRepository;
import com.saep.backend.model.Maca;

@RestController
@RequestMapping("/api/macas")
public class MacaController {

    private final MacaRepository repository;

    public MacaController(MacaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Maca> listar() {
        return repository.findAllByOrderByNomeAsc();
    }
}
