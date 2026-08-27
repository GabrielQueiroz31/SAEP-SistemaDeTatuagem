package com.saep.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.saep.backend.model.Tatuador;
import com.saep.backend.model.Maca;
import com.saep.backend.Repository.MacaRepository;
import com.saep.backend.Repository.TatuadorRepository;

@Configuration
public class DadosIniciais {

    @Bean
    CommandLineRunner popularDados(
        TatuadorRepository tatuadorRepository,
        MacaRepository macaRepository
    ) {
        return args -> {
            if (tatuadorRepository.count() == 0) {
                salvarTatuador(tatuadorRepository, "Ana Silva", "Fine line e minimalista");
                salvarTatuador(tatuadorRepository, "Bruno Santos", "Blackwork e pontilhismo");
                salvarTatuador(tatuadorRepository, "Caio Oliveira", "Realismo");
                salvarTatuador(tatuadorRepository, "Duda Costa", "Aquarela e colorida");
                salvarTatuador(tatuadorRepository, "Enzo Ferreira", "Old school e tradicional");
            } else {
                atualizarNome(tatuadorRepository, "Ana Ink", "Ana Silva");
                atualizarNome(tatuadorRepository, "Bruno Black", "Bruno Santos");
                atualizarNome(tatuadorRepository, "Caio Art", "Caio Oliveira");
                atualizarNome(tatuadorRepository, "Duda Color", "Duda Costa");
                atualizarNome(tatuadorRepository, "Enzo Old", "Enzo Ferreira");
            }

            if (macaRepository.count() == 0) {
                salvarMaca(macaRepository, "Maca 1");
                salvarMaca(macaRepository, "Maca 2");
                salvarMaca(macaRepository, "Maca 3");
                salvarMaca(macaRepository, "Maca 4");
                salvarMaca(macaRepository, "Maca 5");
            }
        };
    }

    private void salvarTatuador(
        TatuadorRepository repository,
        String nome,
        String especialidade
    ) {
        Tatuador tatuador = new Tatuador();
        tatuador.setNome(nome);
        tatuador.setEspecialidade(especialidade);

        repository.save(tatuador);
    }

    private void salvarMaca(MacaRepository repository, String nome) {
        Maca maca = new Maca();
        maca.setNome(nome);

        repository.save(maca);
    }

    private void atualizarNome(
        TatuadorRepository repository,
        String nomeAntigo,
        String nomeNovo
    ) {
        repository.findAll().stream()
            .filter(tatuador -> nomeAntigo.equals(tatuador.getNome()))
            .forEach(tatuador -> {
                tatuador.setNome(nomeNovo);
                repository.save(tatuador);
            });
    }
}
