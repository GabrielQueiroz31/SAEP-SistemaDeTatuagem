package com.saep.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saep.backend.model.Tatuador;

public interface TatuadorRepository extends JpaRepository<Tatuador, Long> {

    List<Tatuador> findByAtivoTrueOrderByNome();
}
