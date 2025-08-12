package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.controller;

import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoConfronto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.service.BuscarRetrospectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Retrospecto")
@RestController
@RequestMapping(value = "/api/clube", produces = MediaType.APPLICATION_JSON_VALUE)
public class BuscarRetrospectoApiController {
    private final BuscarRetrospectoService buscarRetrospectoService;

    public BuscarRetrospectoApiController(BuscarRetrospectoService buscarRetrospectoService) {
        this.buscarRetrospectoService = buscarRetrospectoService;
    }

    @Operation(
            summary = "Busca o retrospecto geral de um clube",
            description = "Retorna dados agregados de jogos, vitórias, empates, derrotas, gols feitos e sofridos. Retorna campos zerados se não houver partidas. 404 se clube não existe."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Retrospecto retornado com sucesso",
            content = @Content(
                    schema = @Schema(implementation = Retrospecto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Clube não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @GetMapping("/{id}/retrospecto")
    public ResponseEntity<Retrospecto> buscarRetrospectoTotalDeUmClube
            (@Parameter(description = "ID do clube", example = "1")
             @PathVariable Long id,
             @Parameter(description = "Filtrar só partidas como mandante", example = "true")
             @RequestParam(required = false) Boolean mandante,
             @Parameter(description = "Filtrar só partidas como visitante", example = "false")
             @RequestParam(required = false) Boolean visitante){
        Retrospecto retrospecto = buscarRetrospectoService.buscarRetrospectoClube(id, mandante, visitante);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(retrospecto);
    }

    @Operation(
            summary = "Busca retrospecto de um clube contra todos adversários",
            description = "Mostra resumo de partidas, vitórias, empates, derrotas, gols feitos/sofridos por adversário. Retorna lista vazia se não houver confrontos. 404 caso o clube não exista."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Retrospecto contra adversários retornado com sucesso",
            content = @Content(
                    schema = @Schema(implementation = RetrospectoAdversariosResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Clube não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @GetMapping("/{id}/retrospectos-adversarios")
    public ResponseEntity<RetrospectoAdversariosResponseDTO> buscarContraAdversarios
            (@Parameter(description = "ID do clube", example = "1")
             @PathVariable Long id,
             @Parameter(description = "Filtrar só partidas como mandante", example = "true")
             @RequestParam(required = false) Boolean mandante,
             @Parameter(description = "Filtrar só partidas como visitante", example = "false")
             @RequestParam(required = false) Boolean visitante)
    {
        RetrospectoAdversariosResponseDTO retrospectosDTO =
                buscarRetrospectoService.buscarRetrospectoClubeContraAdversarios(id, mandante, visitante);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(retrospectosDTO);
    }

    @Operation(
            summary = "Busca retrospecto de confrontos diretos entre dois clubes",
            description = "Retorna retrospecto consolidado e lista de partidas entre dois clubes. Lista vazia e campos zerados se sem confrontos. 404 caso qualquer clube não exista."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Confronto encontrado ou ambos clubes com retrospecto zerado",
            content = @Content(
                    schema = @Schema(implementation = RetrospectoConfronto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Um dos clubes não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @GetMapping("/{idClube}/confronto/{idAdversario}")
    public ResponseEntity<RetrospectoConfronto> buscarConfronto(
            @Parameter(description = "ID do clube principal", example = "1")
            @PathVariable Long idClube,
            @Parameter(description = "ID do adversário", example = "2")
            @PathVariable Long idAdversario
    ) {
        RetrospectoConfronto retrospectoConfronto = buscarRetrospectoService
                .buscarRetrospectoConfronto(idClube, idAdversario);
        return ResponseEntity.status(HttpStatus.OK).body(retrospectoConfronto);
    }
}
