package br.com.meli.teamcubation_partidas_de_futebol.ranking.controller;

import br.com.meli.teamcubation_partidas_de_futebol.ranking.model.Ranking;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Ranking")
@RestController
@RequestMapping("/api/clube/ranking")
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
        - Uma lista vazia é retornada caso não existam clubes para o ranking solicitado.
        """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Ranking retornado com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Ranking.class),
                    examples = {
                            @ExampleObject(
                                    name = "TOTAL_PONTOS",
                                    summary = "Ranking por total de pontos",
                                    value = """
                    [
                        {"nomeClube": "aclube de time", "estadoClube": "AP", "total": 6},
                        {"nomeClube": "clube de time criado", "estadoClube": "SP", "total": 6},
                        {"nomeClube": "Clube de Exemplo Um", "estadoClube": "AM", "total": 6},
                        {"nomeClube": "time teste", "estadoClube": "SP", "total": 5},
                        {"nomeClube": "clube de time atualizado", "estadoClube": "AM", "total": 4}
                    ]
                    """
                            ),
                            @ExampleObject(
                                    name = "TOTAL_VITORIAS",
                                    summary = "Ranking por total de vitórias",
                                    value = """
                    [
                        {"nomeClube": "aclube de time", "estadoClube": "AP", "total": 2},
                        {"nomeClube": "clube de time criado", "estadoClube": "SP", "total": 2},
                        {"nomeClube": "Clube de Exemplo Um", "estadoClube": "AM", "total": 2},
                        {"nomeClube": "clube de time atualizado", "estadoClube": "AM", "total": 1},
                        {"nomeClube": "clube de time criado", "estadoClube": "SC", "total": 1}
                    ]
                    """
                            ),
                            @ExampleObject(
                                    name = "TOTAL_GOLS",
                                    summary = "Ranking por total de gols",
                                    value = """
                    [
                        {"nomeClube": "aclube de time", "estadoClube": "AP", "total": 10},
                        {"nomeClube": "time teste", "estadoClube": "SP", "total": 8},
                        {"nomeClube": "clube de time criado", "estadoClube": "SP", "total": 7},
                        {"nomeClube": "Clube de Exemplo Um", "estadoClube": "AM", "total": 7},
                        {"nomeClube": "clube de time atualizado", "estadoClube": "AM", "total": 6}
                    ]
                    """
                            ),
                            @ExampleObject(
                                    name = "TOTAL_JOGOS",
                                    summary = "Ranking por total de jogos",
                                    value = """
                    [
                        {"nomeClube": "clube de time atualizado", "estadoClube": "AM", "total": 4},
                        {"nomeClube": "aclube de time", "estadoClube": "AP", "total": 4},
                        {"nomeClube": "clube de time", "estadoClube": "AP", "total": 3},
                        {"nomeClube": "time teste", "estadoClube": "SP", "total": 3},
                        {"nomeClube": "clube de time criado", "estadoClube": "SP", "total": 2}
                    ]
                    """
                            ),
                            @ExampleObject(
                                    name = "lista-vazia",
                                    summary = "Nenhum clube atende ao critério",
                                    value = "[]"
                            )
                    }
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
