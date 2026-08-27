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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.saep.backend.Repository.AgendamentoRepository;
import com.saep.backend.Repository.ClienteRepository;
import com.saep.backend.Repository.MacaRepository;
import com.saep.backend.Repository.TatuadorRepository;
import com.saep.backend.model.Agendamento;
import com.saep.backend.model.Cliente;
import com.saep.backend.model.Maca;
import com.saep.backend.model.Tatuador;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final MacaRepository macaRepository;
    private final TatuadorRepository tatuadorRepository;

    public AgendamentoController(
        AgendamentoRepository agendamentoRepository,
        ClienteRepository clienteRepository,
        MacaRepository macaRepository,
        TatuadorRepository tatuadorRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.macaRepository = macaRepository;
        this.tatuadorRepository = tatuadorRepository;
    }

    @GetMapping
    public List<Agendamento> listar() {
        return agendamentoRepository.findAllByOrderByDataAscHoraAsc();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agendamento criar(@Valid @RequestBody Agendamento dados) {
        return salvar(new Agendamento(), dados);
    }

    @PutMapping("/{id}")
    public Agendamento atualizar(
        @PathVariable Long id,
        @Valid @RequestBody Agendamento dados
    ) {
        return salvar(buscarAgendamentoPorId(id), dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        agendamentoRepository.delete(buscarAgendamentoPorId(id));
    }

    private Agendamento salvar(Agendamento agendamento, Agendamento dados) {
        validarRelacionamentos(dados);

        Long clienteId = dados.getCliente().getId();
        Long macaId = dados.getMaca().getId();
        Long tatuadorId = dados.getTatuador().getId();

        verificarConflitosDeHorario(agendamento, dados, macaId, tatuadorId);

        agendamento.setCliente(buscarClientePorId(clienteId));
        agendamento.setMaca(buscarMacaPorId(macaId));
        agendamento.setTatuador(buscarTatuadorPorId(tatuadorId));
        agendamento.setData(dados.getData());
        agendamento.setHora(dados.getHora());
        agendamento.setServico(dados.getServico());
        agendamento.setObservacao(dados.getObservacao());

        return agendamentoRepository.save(agendamento);
    }

    private void validarRelacionamentos(Agendamento dados) {
        boolean clienteInvalido = dados.getCliente() == null
            || dados.getCliente().getId() == null;
        boolean macaInvalida = dados.getMaca() == null
            || dados.getMaca().getId() == null;
        boolean tatuadorInvalido = dados.getTatuador() == null
            || dados.getTatuador().getId() == null;

        if (clienteInvalido || macaInvalida || tatuadorInvalido) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Cliente, maca e tatuador são obrigatórios."
            );
        }
    }

    private void verificarConflitosDeHorario(
        Agendamento agendamento,
        Agendamento dados,
        Long macaId,
        Long tatuadorId
    ) {
        boolean tatuadorOcupado;
        boolean macaOcupada;

        if (agendamento.getId() == null) {
            tatuadorOcupado = agendamentoRepository
                .existsByTatuadorIdAndDataAndHora(
                    tatuadorId,
                    dados.getData(),
                    dados.getHora()
                );
            macaOcupada = agendamentoRepository
                .existsByMacaIdAndDataAndHora(
                    macaId,
                    dados.getData(),
                    dados.getHora()
                );
        } else {
            tatuadorOcupado = agendamentoRepository
                .existsByTatuadorIdAndDataAndHoraAndIdNot(
                    tatuadorId,
                    dados.getData(),
                    dados.getHora(),
                    agendamento.getId()
                );
            macaOcupada = agendamentoRepository
                .existsByMacaIdAndDataAndHoraAndIdNot(
                    macaId,
                    dados.getData(),
                    dados.getHora(),
                    agendamento.getId()
                );
        }

        if (tatuadorOcupado) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Este tatuador já possui um agendamento nesta data e horário."
            );
        }

        if (macaOcupada) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Esta maca já está em uso nesta data e horário."
            );
        }
    }

    private Agendamento buscarAgendamentoPorId(Long id) {
        return agendamentoRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Agendamento não encontrado."
            )
        );
    }

    private Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente inválido.")
        );
    }

    private Maca buscarMacaPorId(Long id) {
        return macaRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maca inválida.")
        );
    }

    private Tatuador buscarTatuadorPorId(Long id) {
        return tatuadorRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tatuador inválido.")
        );
    }
}
