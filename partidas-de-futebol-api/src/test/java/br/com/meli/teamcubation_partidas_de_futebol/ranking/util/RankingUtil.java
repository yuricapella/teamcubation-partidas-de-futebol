package br.com.meli.teamcubation_partidas_de_futebol.ranking.util;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.model.*;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.strategy.*;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;

import java.util.List;

public class RankingUtil {

    public static DadosRankingMock criarMockRanking(TipoRanking tipo, Clube clube1, Clube clube2, Retrospecto retrospecto1, Retrospecto retrospecto2) {
        return switch (tipo) {
            case TOTAL_PONTOS -> new DadosRankingMock(
                    List.of(
                            new RankingPontos(clube1, retrospecto1)
                    ),
                    6, 0
            );
            case TOTAL_GOLS -> new DadosRankingMock(
                    List.of(
                            new RankingGols(clube1, retrospecto1),
                            new RankingGols(clube2, retrospecto2)
                    ),
                    7, 5
            );
            case TOTAL_VITORIAS -> new DadosRankingMock(
                    List.of(
                            new RankingVitorias(clube1, retrospecto1)
                    ),
                    2, 0
            );
            case TOTAL_JOGOS -> new DadosRankingMock(
                    List.of(
                            new RankingJogos(clube1, retrospecto1),
                            new RankingJogos(clube2, retrospecto2)
                    ),
                    2, 2
            );
        };
    }

    public record DadosRankingMock(List<? extends Ranking> lista, int totalClube1, int totalClube2) {}

    public static CalculadoraRankingStrategy criarCalculadoraRanking(TipoRanking tipo) {
        return switch (tipo) {
            case TOTAL_PONTOS -> new RankingPontosStrategy();
            case TOTAL_GOLS -> new RankingGolsStrategy();
            case TOTAL_VITORIAS -> new RankingVitoriasStrategy();
            case TOTAL_JOGOS -> new RankingJogosStrategy();
        };
    }
}
