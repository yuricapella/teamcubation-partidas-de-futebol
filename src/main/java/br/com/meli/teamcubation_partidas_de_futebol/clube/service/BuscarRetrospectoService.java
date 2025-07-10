package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.RetrospectoClube;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarRetrospectoService {
    private final BuscarClubeService buscarClubeService;
    private final PartidaRepository partidaRepository;

    public BuscarRetrospectoService(BuscarClubeService buscarClubeService, PartidaRepository partidaRepository) {
        this.buscarClubeService = buscarClubeService;
        this.partidaRepository = partidaRepository;
    }

    public RetrospectoClube buscarRetrospecto(Long id) {
        Clube clubeRetornado = buscarClubeService.buscarClubePorId(id);
        List<Partida> partidas = partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(id,id);
        RetrospectoClube retrospecto = new RetrospectoClube(clubeRetornado, partidas);
        return retrospecto;
    }
}
