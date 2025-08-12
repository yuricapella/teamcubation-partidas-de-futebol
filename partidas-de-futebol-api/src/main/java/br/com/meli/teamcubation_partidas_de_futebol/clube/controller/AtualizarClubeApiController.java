package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.AtualizarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.ClubeResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.AtualizarClubeService;
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

@Tag(name = "Clubes")
@RestController
@RequestMapping(value = "/api/clube/atualizar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AtualizarClubeApiController {
    private final AtualizarClubeService atualizarClubeService;

    public AtualizarClubeApiController(AtualizarClubeService atualizarClubeService) {
        this.atualizarClubeService = atualizarClubeService;
    }


    @Operation(
            summary = "Atualiza os dados de um clube",
            description = "Atualiza nome, sigla do estado e data de criação do clube. Retorna 200 em caso de sucesso ou erro específico em cada cenário de exceção."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Clube atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ClubeResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Campos inválidos ou estado inexistente",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples =
                            @ExampleObject(
                                    name = "campos-invalidos",
                                    summary = "Request com campos inválidos"
                            )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Clube não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            name = "clube-nao-encontrado",
                            summary = "Clube não existe na base de dados"
                    )
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflito de dados (data inválida ou duplicidade de clube)",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples =
                            @ExampleObject(
                                    name = "data-criacao-posterior",
                                    summary = "Data de criação após uma partida cadastrada"
                            )
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> atualizar(
            @RequestBody @Valid AtualizarClubeRequestDTO clubeAtualizado,
            @Parameter(description = "ID do clube. Deve ser um número inteiro ou long.", example = "1")
            @PathVariable Long id) {

        Clube clubeAlterado = atualizarClubeService.atualizar(clubeAtualizado, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubeResponseMapper.toClubeResponseDTO(clubeAlterado));
    }

}
