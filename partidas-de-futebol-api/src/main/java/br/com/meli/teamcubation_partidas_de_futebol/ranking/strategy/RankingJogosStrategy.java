package br.com.meli.teamcubation_partidas_de_futebol.ranking.strategy;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.model.RankingJogos;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.util.TipoRanking;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class RankingJogosStrategy implements CalculadoraRankingStrategy {
    public TipoRanking getTipo() { return TipoRanking.TOTAL_JOGOS; }

    public List<RankingJogos> calcular(List<Clube> clubes, Map<Long, Retrospecto> retrospectosPorClube) {
        return clubes.stream()
                .map(clube -> new RankingJogos(clube, retrospectosPorClube.get(clube.getId())))
                .filter(retrospecto -> retrospecto.getTotal() > 0)
                .sorted(Comparator.comparingInt(RankingJogos::getTotal).reversed())
                .toList();
    }
}
