package br.com.meli.teamcubation_partidas_de_futebol.estadio.controller;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CriarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioEnderecoResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.CriarEstadioService;
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

@Tag(name = "Estádios")
@RestController
@RequestMapping(value = "/api/estadio/criar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class CriarEstadioApiController {
    private final CriarEstadioService criarEstadioService;

    public CriarEstadioApiController(CriarEstadioService criarEstadioService) {
        this.criarEstadioService = criarEstadioService;
    }

    @Operation(
            summary = "Cria um novo estádio",
            description = "Cadastra um novo estádio informando nome e cep. Retorna 201 com os dados completos e endereço resolvido por VIACEP."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Estádio criado com sucesso",
            content = @Content(
                    schema = @Schema(implementation = EstadioEnderecoResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Campos inválidos",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples =
                            @ExampleObject(
                                    name = "Nome e cep obrigatórios",
                                    summary = "nome ou cep inválido/nulo"
                                    )
                            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Estádio com nome duplicado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @PostMapping
    public ResponseEntity<EstadioEnderecoResponseDTO> criar(@RequestBody @Valid CriarEstadioRequestDTO criarEstadioRequestDTO) {
        EstadioEnderecoResponseDTO estadioCriado = criarEstadioService.criarEstadio(criarEstadioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(estadioCriado);
    }

}
