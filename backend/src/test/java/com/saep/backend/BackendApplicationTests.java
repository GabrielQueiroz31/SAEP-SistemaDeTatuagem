package com.saep.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.saep.backend.controller.AgendamentoController;
import com.saep.backend.model.Agendamento;
import com.saep.backend.model.Cliente;
import com.saep.backend.model.Maca;
import com.saep.backend.model.Tatuador;
import com.saep.backend.Repository.AgendamentoRepository;
import com.saep.backend.Repository.ClienteRepository;
import com.saep.backend.Repository.MacaRepository;
import com.saep.backend.Repository.TatuadorRepository;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private AgendamentoController agendamentoController;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MacaRepository macaRepository;

    @Autowired
    private TatuadorRepository tatuadorRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void naoPermiteDoisClientesComMesmoTatuadorDataEHora() {
        agendamentoRepository.deleteAll();
        clienteRepository.deleteAll();

        Cliente primeiroCliente = salvarCliente(
            "Marina",
            "111.111.111-11"
        );
        Cliente segundoCliente = salvarCliente(
            "Rafael",
            "222.222.222-22"
        );

        Tatuador tatuador = tatuadorRepository
            .findByAtivoTrueOrderByNome()
            .getFirst();
        Maca maca = macaRepository.findAllByOrderByNomeAsc().getFirst();

        Agendamento primeiro = agendamentoController.criar(
            novoAgendamento(primeiroCliente, maca, tatuador)
        );

        assertNotNull(primeiro.getId());

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> agendamentoController.criar(
                novoAgendamento(segundoCliente, maca, tatuador)
            )
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
    }

    @Test
    void naoPermiteUsarAMesmaMacaNaMesmaDataEHora() {
        agendamentoRepository.deleteAll();
        clienteRepository.deleteAll();

        Cliente primeiroCliente = salvarCliente("Julia", "333.333.333-33");
        Cliente segundoCliente = salvarCliente("Pedro", "444.444.444-44");
        Maca maca = macaRepository.findAllByOrderByNomeAsc().getFirst();
        List<Tatuador> tatuadores = tatuadorRepository
            .findByAtivoTrueOrderByNome();

        agendamentoController.criar(
            novoAgendamento(primeiroCliente, maca, tatuadores.get(0))
        );

        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> agendamentoController.criar(
                novoAgendamento(segundoCliente, maca, tatuadores.get(1))
            )
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
    }

    private Cliente salvarCliente(String nome, String documento) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setDocumento(documento);
        cliente.setTelefone("(47) 99999-9999");
        cliente.setEmail(nome.toLowerCase() + "@teste.com");

        return clienteRepository.save(cliente);
    }

    private Agendamento novoAgendamento(
        Cliente cliente,
        Maca maca,
        Tatuador tatuador
    ) {
        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setMaca(maca);
        agendamento.setTatuador(tatuador);
        agendamento.setData(LocalDate.of(2026, 9, 10));
        agendamento.setHora(LocalTime.of(14, 0));
        agendamento.setServico("Tatuagem");

        return agendamento;
    }
}
