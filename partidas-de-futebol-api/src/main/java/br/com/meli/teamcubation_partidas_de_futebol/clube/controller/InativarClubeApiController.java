package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.service.InativarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Clubes")
@RestController
@RequestMapping(value = "/api/clube/inativar", produces = MediaType.APPLICATION_JSON_VALUE)
public class InativarClubeApiController {
    private final InativarClubeService inativarClubeService;

    public InativarClubeApiController(InativarClubeService inativarClubeService) {
        this.inativarClubeService = inativarClubeService;
    }

    @Operation(
            summary = "Inativa um clube por ID (soft delete)",
            description = "Desativa um clube existente (ativo = false). Retorna 204 caso sucesso. Retorna 404 se não encontrar o clube."
    )
    @ApiResponse(
            responseCode = "204 No Content",
            description = "Clube inativado com sucesso"
    )
    @ApiResponse(
            responseCode = "404 Not Found",
            description = "Clube não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            name = "clube-nao-encontrado",
                            summary = "Tentativa de inativar clube inexistente",
                            value = """
                {
                    "codigoErro": "CLUBE_NAO_ENCONTRADO",
                    "dataHora": "04/08/2025 12:00:00",
                    "mensagem": "Clube com id 999 não encontrado.",
                    "errors": null
                }
                """
                    )
            )
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do clube a ser inativado", example = "1")
            @PathVariable Long id) {
        inativarClubeService.inativarClubePorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
