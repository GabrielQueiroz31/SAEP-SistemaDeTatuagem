package com.saep.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.saep.backend.model.Cliente;
import com.saep.backend.Repository.ClienteRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Cliente> listar(
        @RequestParam(required = false) String busca
    ) {
        if (busca == null || busca.isBlank()) {
            return repository.findAll();
        }

        return repository
            .findByNomeContainingIgnoreCaseOrDocumentoContainingIgnoreCase(
                busca,
                busca
            );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente criar(@Valid @RequestBody Cliente cliente) {
        return repository.save(cliente);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(
        @PathVariable Long id,
        @Valid @RequestBody Cliente dados
    ) {
        Cliente cliente = buscarPorId(id);

        cliente.setNome(dados.getNome());
        cliente.setDocumento(dados.getDocumento());
        cliente.setTelefone(dados.getTelefone());
        cliente.setEmail(dados.getEmail());

        return repository.save(cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        Cliente cliente = buscarPorId(id);
        repository.delete(cliente);
    }

    private Cliente buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cliente não encontrado."
            )
        );
    }
}
