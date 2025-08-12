package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.CriarPartidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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

@Tag(name = "Partidas")
@RestController
@RequestMapping(value = "/api/partida/criar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class CriarPartidaApiController {
    private final CriarPartidaService criarPartidaService;

    public CriarPartidaApiController(CriarPartidaService criarPartidaService) {
        this.criarPartidaService = criarPartidaService;
    }

    @Operation(
            summary = "Cadastra uma nova partida",
            description = "Cria uma partida informando clubes, estádio, gols e data/hora. Retorna 201 ao sucesso. Aplica todas regras com mensagens claras para cenários inválidos."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Partida criada com sucesso",
            content = @Content(
                    schema = @Schema(implementation = PartidaResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos (campos obrigatórios, gols negativos, data futura, clubes iguais)",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Clube ou estádio não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflitos de negócio: data anterior à criação de clube, clube inativo, partidas próximas, estádio já ocupado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @PostMapping
    public ResponseEntity<PartidaResponseDTO> criar(@RequestBody @Valid CriarPartidaRequestDTO criarPartidaRequestDTO) {
        Partida partidaCriada = criarPartidaService.criarPartida(criarPartidaRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PartidaResponseMapper.toPartidaResponseDTO(partidaCriada));
    }

}
