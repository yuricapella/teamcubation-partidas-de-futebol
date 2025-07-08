package br.com.meli.teamcubation_partidas_de_futebol.partida.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.BuscarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.AtualizarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.AtualizarPartidaRequestMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaValidator;
import org.springframework.stereotype.Service;

@Service
public class AtualizarPartidaService {
    private final PartidaRepository partidaRepository;
    private final BuscarClubeService buscarClubeService;
    private final BuscarEstadioService buscarEstadioService;
    private final BuscarPartidaService buscarPartidaService;
    private final PartidaValidator partidaValidator;

    public AtualizarPartidaService(
            PartidaRepository partidaRepository, BuscarEstadioService buscarEstadioService,
            BuscarClubeService buscarClubeService, BuscarPartidaService buscarPartidaService, PartidaValidator partidaValidator)
    {
        this.partidaRepository = partidaRepository;
        this.buscarClubeService = buscarClubeService;
        this.buscarEstadioService = buscarEstadioService;
        this.buscarPartidaService = buscarPartidaService;
        this.partidaValidator = partidaValidator;
    }

    public Partida atualizarPartida(AtualizarPartidaRequestDTO dadosParaAtualizar, Long idPartida) {
        Partida partidaComDadosAntigos = buscarPartidaService.buscarPartidaPorId(idPartida);
        Clube clubeMandante = buscarClubeService.buscarClubePorId(dadosParaAtualizar.getClubeMandanteId());
        Clube clubeVisitante = buscarClubeService.buscarClubePorId(dadosParaAtualizar.getClubeVisitanteId());
        Estadio estadio = buscarEstadioService.buscarEstadioPorId(dadosParaAtualizar.getEstadioId());
        partidaValidator.validarAtualizacaoDePartidas(clubeMandante,clubeVisitante,estadio,dadosParaAtualizar);
        Partida partidaAtualizada = AtualizarPartidaRequestMapper.updateEntity(dadosParaAtualizar, partidaComDadosAntigos, clubeMandante, clubeVisitante, estadio);
        return partidaRepository.save(partidaAtualizada);
    }
}
