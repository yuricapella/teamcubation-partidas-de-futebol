package br.com.meli.teamcubation_partidas_de_futebol.partida.service;

import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.PartidaNaoEncontradaException;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarPartidaService {
    private final PartidaRepository partidaRepository;

    public BuscarPartidaService(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    public Partida buscarPartidaPorId(Long id) {
        Optional<Partida> partidaOptional = partidaRepository.findById(id);
        return partidaOptional.orElseThrow(() -> new PartidaNaoEncontradaException(id));
    }
}
