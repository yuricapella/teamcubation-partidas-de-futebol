package br.com.meli.teamcubation_partidas_de_futebol.partida.service;

import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.PartidaNaoEncontradaException;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Page<Partida> listarPartidasFiltradas(Long clubeId, Long estadioId, Boolean goleada, Pageable pageable) {
        Page<Partida> partidas = partidaRepository.findByFiltros(clubeId,estadioId,pageable);

        if (goleada == null) {
            return partidas;
        }

        List<Partida> partidasFiltradas = partidas.stream()
                .filter(partida -> partida.isGoleada() == goleada)
                .collect(Collectors.toList());

        return new PageImpl<>(partidasFiltradas, pageable, partidasFiltradas.size());
    }
}
