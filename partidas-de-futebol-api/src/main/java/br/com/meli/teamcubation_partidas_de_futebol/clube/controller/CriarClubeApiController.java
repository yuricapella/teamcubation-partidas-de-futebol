package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.CriarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.CriarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Clubes")
@RestController
@RequestMapping(value = "/api/clube/criar",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CriarClubeApiController {
    private final CriarClubeService criarClubeService;

    public CriarClubeApiController(CriarClubeService criarClubeService) {
        this.criarClubeService = criarClubeService;
    }

    @Operation(
            summary = "Cria um novo clube",
            description = "Cadastra um novo clube. Retorna 201 ao sucesso."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Clube criado com sucesso",
            content = @Content(
                    schema = @Schema(implementation = ClubeResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Campos inválidos ou estado inexistente",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples =
                            @ExampleObject(
                                    name = "estado-inexistente",
                                    summary = "Tentativa de criar clube com estado inexistente"
                            )
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Já existe clube com esse nome no estado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @PostMapping
    public ResponseEntity<ClubeResponseDTO> criar(@RequestBody @Valid CriarClubeRequestDTO clubeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(criarClubeService.criarClube(clubeDTO));
    }
}
