package br.com.meli.teamcubation_partidas_de_futebol.job;

import br.com.meli.teamcubation_partidas_de_futebol.ranking.util.RankingPrinterUtil;
import br.com.meli.teamcubation_partidas_de_futebol.util.TimezoneUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobCalcularRankingDiario {
    private final RankingPrinterUtil rankingPrinterUtil;


    public JobCalcularRankingDiario(RankingPrinterUtil rankingPrinterUtil) {
        this.rankingPrinterUtil = rankingPrinterUtil;
    }

    @Scheduled(cron = "${job.calcular-ranking-diario.cron}", zone = TimezoneUtil.ZONE_SAO_PAULO)
    public void calcularRankingDiario() {
        rankingPrinterUtil.imprimirTodosOsRankings();
    }
}
