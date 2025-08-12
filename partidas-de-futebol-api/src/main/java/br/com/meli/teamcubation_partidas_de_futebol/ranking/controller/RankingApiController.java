package br.com.meli.teamcubation_partidas_de_futebol.ranking.controller;

import br.com.meli.teamcubation_partidas_de_futebol.ranking.model.Ranking;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Ranking")
@RestController
@RequestMapping(value = "/api/clube/ranking", produces = MediaType.APPLICATION_JSON_VALUE)
public class RankingApiController {
    private final RankingService rankingService;

    public RankingApiController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Operation(
            summary = "Obtém ranking de clubes conforme o critério",
            description = """
        Retorna ranking dos clubes ordenado conforme critério escolhido (tipoRanking):
        - TOTAL_PONTOS (pontos, vitória=3, empate=1)
        - TOTAL_VITORIAS
        - TOTAL_GOLS
        - TOTAL_JOGOS
        - Uma lista vazia é retornada caso não existam clubes e partidas para o ranking solicitado.
        """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de Ranking retornado, lista vazia se clubes estiverem com pontuação zerada ou não existirem partidas",
            content = @Content(
                    schema = @Schema(implementation = Ranking.class)
            )
    )
    @GetMapping
    public ResponseEntity<List<? extends Ranking>> calcularRanking
            ( @Parameter(
                    description = "Critério do ranking: TOTAL_PONTOS, TOTAL_VITORIAS, TOTAL_GOLS ou TOTAL_JOGOS",
                    example = "TOTAL_PONTOS"
            )
                    @RequestParam("tipoRanking") String tipoRankingString) {
        List<? extends Ranking> ranking = rankingService.calcularRanking(tipoRankingString);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ranking);
    }
}
