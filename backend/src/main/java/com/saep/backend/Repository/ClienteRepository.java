package com.saep.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saep.backend.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeContainingIgnoreCaseOrDocumentoContainingIgnoreCase(
        String nome,
        String documento
    );
}
