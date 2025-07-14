package br.com.meli.teamcubation_partidas_de_futebol.ranking.controller;

import br.com.meli.teamcubation_partidas_de_futebol.ranking.model.Ranking;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.service.RankingService;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.util.TipoRanking;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clube/ranking")
public class RankingApiController {
    private final RankingService rankingService;

    public RankingApiController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping
    public ResponseEntity<List<? extends Ranking>> calcularRanking
            (@RequestParam("tipoRanking") TipoRanking tipo) {
        List<? extends Ranking> ranking = rankingService.calcularRanking(tipo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ranking);
    }
}
