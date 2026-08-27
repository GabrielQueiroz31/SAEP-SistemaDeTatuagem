package com.saep.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saep.backend.model.Maca;

public interface MacaRepository extends JpaRepository<Maca, Long> {

    List<Maca> findAllByOrderByNomeAsc();
}
