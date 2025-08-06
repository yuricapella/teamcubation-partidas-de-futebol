package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.DeletarPartidaService;
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

@Tag(name = "Partidas")
@RestController
@RequestMapping(value = "/api/partida/deletar", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeletarPartidaApiController {
    private final DeletarPartidaService deletarPartidaService;

    public DeletarPartidaApiController(DeletarPartidaService deletarPartidaService) {
        this.deletarPartidaService = deletarPartidaService;
    }

    @Operation(
            summary = "Deleta uma partida",
            description = "Remove definitivamente uma partida pelo ID. Retorna 204 NO CONTENT se sucesso. Caso não exista, retorna 404 NOT FOUND."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Partida deletada com sucesso"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Partida não encontrada",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            name = "partida-nao-encontrada",
                            summary = "Partida não encontrada",
                            value = """
            {
                "codigoErro": "PARTIDA_NAO_ENCONTRADA",
                "dataHora": "04/08/2025 23:05:00",
                "mensagem": "Partida com id 999 não encontrada.",
                "errors": null
            }
            """
                    )
            )
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarPartidaPorId(
            @Parameter(description = "ID da partida a ser excluída", example = "1")
            @PathVariable Long id) {

        deletarPartidaService.deletarPartidaPorId(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
