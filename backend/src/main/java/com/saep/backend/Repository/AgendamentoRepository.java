package com.saep.backend.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saep.backend.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByTatuadorIdAndDataAndHora(
        Long tatuadorId,
        LocalDate data,
        LocalTime hora
    );

    boolean existsByTatuadorIdAndDataAndHoraAndIdNot(
        Long tatuadorId,
        LocalDate data,
        LocalTime hora,
        Long id
    );

    boolean existsByMacaIdAndDataAndHora(
        Long macaId,
        LocalDate data,
        LocalTime hora
    );

    boolean existsByMacaIdAndDataAndHoraAndIdNot(
        Long macaId,
        LocalDate data,
        LocalTime hora,
        Long id
    );

    List<Agendamento> findAllByOrderByDataAscHoraAsc();
}
