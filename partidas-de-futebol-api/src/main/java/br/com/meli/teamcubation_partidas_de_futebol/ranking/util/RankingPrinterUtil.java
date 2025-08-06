package br.com.meli.teamcubation_partidas_de_futebol.ranking.util;

import br.com.meli.teamcubation_partidas_de_futebol.ranking.model.Ranking;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.service.RankingService;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class RankingPrinterUtil {
    private final RankingService rankingService;

    public RankingPrinterUtil(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    public Map<TipoRanking, List<? extends Ranking>> calcularTodosRankings() {
        Map<TipoRanking, List<? extends Ranking>> rankingsMap = new EnumMap<>(TipoRanking.class);
        for (TipoRanking tipo : TipoRanking.values()) {
            List<? extends Ranking> ranking = rankingService.calcularRanking(tipo.name());
            rankingsMap.put(tipo, ranking);
        }
        return rankingsMap;
    }

    public void imprimirRankingsGrupados(Map<TipoRanking, List<? extends Ranking>> rankingsMap) {
        for (Map.Entry<TipoRanking, List<? extends Ranking>> entry : rankingsMap.entrySet()) {
            TipoRanking tipo = entry.getKey();
            List<? extends Ranking> ranking = entry.getValue();
            System.out.println("\n=== Ranking: " + tipo.name() + " ===");
            System.out.printf("%-25s %-10s %s%n", "Clube", "Estado", "Total");
            for (Ranking r : ranking) {
                System.out.printf("%-25s %-10s %d%n", r.getNomeClube(), r.getEstadoClube(), r.getTotal());
            }
        }
    }

    public void imprimirTodosOsRankings() {
        Map<TipoRanking, List<? extends Ranking>> rankingsMap = calcularTodosRankings();
        imprimirRankingsGrupados(rankingsMap);
    }
}
