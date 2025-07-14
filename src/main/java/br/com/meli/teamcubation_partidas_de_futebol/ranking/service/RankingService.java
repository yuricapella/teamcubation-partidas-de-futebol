package br.com.meli.teamcubation_partidas_de_futebol.ranking.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.strategy.CalculadoraRankingStrategy;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.model.Ranking;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.util.TipoRanking;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RankingService {
    private final List<CalculadoraRankingStrategy> strategies;
    private final PartidaRepository partidaRepository;
    private final ClubeRepository clubeRepository;

    public RankingService(List<CalculadoraRankingStrategy> strategies, PartidaRepository partidaRepository, ClubeRepository clubeRepository) {
        this.strategies = strategies;
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
    }

    public List<? extends Ranking> calcularRanking(TipoRanking tipo) {
        List<Clube> clubes = clubeRepository.findAll();
        Map<Long, Retrospecto> retrospectosPorClube = clubes.stream()
                .collect(Collectors.toMap(
                        Clube::getId,
                        clube -> {
                            List<Partida> partidasDoClube = partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(clube.getId(),clube.getId());
                            return new Retrospecto(clube, partidasDoClube);
                        }
                ));
        CalculadoraRankingStrategy strategy = strategies.stream()
                .filter(s -> s.getTipo().equals(tipo))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo do ranking inválido"));
        return strategy.calcular(clubes, retrospectosPorClube);
    }
}
