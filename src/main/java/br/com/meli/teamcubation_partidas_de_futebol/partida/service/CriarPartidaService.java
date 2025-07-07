package br.com.meli.teamcubation_partidas_de_futebol.partida.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.BuscarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.CriarPartidaRequestMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaValidator;
import org.springframework.stereotype.Service;

@Service
public class CriarPartidaService {
    private final PartidaRepository partidaRepository;
    private final BuscarClubeService buscarClubeService;
    private final BuscarEstadioService buscarEstadioService;
    private final PartidaValidator partidaValidator;

    public CriarPartidaService(
            PartidaRepository partidaRepository, BuscarEstadioService buscarEstadioService,
            BuscarClubeService buscarClubeService, PartidaValidator partidaValidator)
    {
        this.partidaRepository = partidaRepository;
        this.buscarClubeService = buscarClubeService;
        this.buscarEstadioService = buscarEstadioService;
        this.partidaValidator = partidaValidator;
    }

    public Partida criarPartida(CriarPartidaRequestDTO partidaACriar) {
        Clube clubeMandante = buscarClubeService.buscarClubePorId(partidaACriar.getClubeMandanteId());
        Clube clubeVisitante = buscarClubeService.buscarClubePorId(partidaACriar.getClubeVisitanteId());
        Estadio estadio = buscarEstadioService.buscarEstadioPorId(partidaACriar.getEstadioId());
        partidaValidator.validarCriacaoDePartidas(clubeMandante,clubeVisitante,estadio,partidaACriar);
        Partida partidaCriada = CriarPartidaRequestMapper.toEntity(partidaACriar, clubeMandante, clubeVisitante, estadio);
        return partidaRepository.save(partidaCriada);
    }
}
