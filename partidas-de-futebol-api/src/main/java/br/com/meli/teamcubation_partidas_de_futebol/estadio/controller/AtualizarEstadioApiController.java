package br.com.meli.teamcubation_partidas_de_futebol.estadio.controller;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.AtualizarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioEnderecoResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.AtualizarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Estádios")
@RestController
@RequestMapping(value = "/api/estadio/atualizar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AtualizarEstadioApiController {
    private final AtualizarEstadioService atualizarEstadioService;

    public AtualizarEstadioApiController(AtualizarEstadioService atualizarEstadioService) {
        this.atualizarEstadioService = atualizarEstadioService;
    }

    @Operation(
            summary = "Atualiza os dados de um estádio",
            description = "Edita nome e/ou endereço de estádio. Retorna 200 em caso de sucesso. Retorna 400 para dados inválidos, 409 para nome já existente e 404 para estádio não encontrado."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Estádio atualizado com sucesso",
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
            responseCode = "404",
            description = "Estádio não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Estádio com nome duplicado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @PutMapping(value = "/{id}")
    public ResponseEntity<EstadioEnderecoResponseDTO> atualizar
            (@RequestBody @Valid AtualizarEstadioRequestDTO atualizarEstadioRequestDTO,
             @Parameter(description = "ID do estádio a ser atualizado", example = "1")
             @PathVariable Long id) {

        EstadioEnderecoResponseDTO estadioAtualizado = atualizarEstadioService.atualizarEstadio(atualizarEstadioRequestDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(estadioAtualizado);
    }

}
