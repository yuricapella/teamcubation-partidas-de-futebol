package br.com.meli.teamcubation_partidas_de_futebol.ranking.strategy;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.model.Ranking;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.util.TipoRanking;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface CalculadoraRankingStrategy {
    List<? extends Ranking> calcular
    (List<Clube> clubes, Map<Long, Retrospecto> retrospectosPorClube);

    TipoRanking getTipo();
}
