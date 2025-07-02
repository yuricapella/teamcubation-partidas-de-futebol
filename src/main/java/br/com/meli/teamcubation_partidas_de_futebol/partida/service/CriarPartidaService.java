package br.com.meli.teamcubation_partidas_de_futebol.partida.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.BuscarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.CriarPartidaRequestMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarPartidaService {
    PartidaRepository partidaRepository;
    BuscarClubeService buscarClubeService;
    BuscarEstadioService buscarEstadioService;

    public CriarPartidaService(PartidaRepository partidaRepository, BuscarEstadioService buscarEstadioService, BuscarClubeService buscarClubeService) {
        this.partidaRepository = partidaRepository;
        this.buscarClubeService = buscarClubeService;
        this.buscarEstadioService = buscarEstadioService;

    }

    public Partida criarPartida(CriarPartidaRequestDTO partidaACriar) {
        Clube clubeMandante = buscarClubeService.buscarClubePorId(partidaACriar.getClubeMandanteId());
        Clube clubeVisitante = buscarClubeService.buscarClubePorId(partidaACriar.getClubeVisitanteId());
        Estadio estadio = buscarEstadioService.buscarEstadioPorId(partidaACriar.getEstadioId());
        Partida partidaCriada = CriarPartidaRequestMapper.toEntity(partidaACriar, clubeMandante, clubeVisitante, estadio);
        return partidaRepository.save(partidaCriada);
    }
}
