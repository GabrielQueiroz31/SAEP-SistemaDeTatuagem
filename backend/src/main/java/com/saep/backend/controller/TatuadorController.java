package com.saep.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saep.backend.model.Tatuador;
import com.saep.backend.Repository.TatuadorRepository;

@RestController
@RequestMapping("/api/tatuadores")
public class TatuadorController {

    private final TatuadorRepository repository;

    public TatuadorController(TatuadorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Tatuador> listar() {
        return repository.findByAtivoTrueOrderByNome();
    }
}
