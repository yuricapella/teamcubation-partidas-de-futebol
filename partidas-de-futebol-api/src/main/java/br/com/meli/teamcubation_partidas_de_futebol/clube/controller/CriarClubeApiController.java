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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Clubes")
@RestController
@RequestMapping("/api/clube/criar")
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
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClubeResponseDTO.class),
                    examples = @ExampleObject(
                            value = """
                {
                  "nome":"Clube de Exemplo dezoito",
                  "siglaEstado":"AM",
                  "dataCriacao":"2025-05-13"
                }
                """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Campos inválidos ou estado inexistente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = {
                            @ExampleObject(
                                    name = "campos-invalidos",
                                    summary = "Request com campos inválidos",
                                    value = """
                {
                    "codigoErro": "CAMPO_INVALIDO",
                    "dataHora": "04/08/2025 11:02:56",
                    "mensagem": "Invalid request content.",
                    "errors": {
                        "siglaEstado": "A sigla do estado só pode ter 2 letras.",
                        "nome": "O nome tem que ter no minimo duas letras;",
                        "dataCriacao": "A data de criação não pode ser futura"
                    }
                }
                """
                            ),
                            @ExampleObject(
                                    name = "estado-inexistente",
                                    summary = "Tentativa de criar clube com estado inexistente",
                                    value = """
                {
                    "codigoErro": "ESTADO_INEXISTENTE",
                    "dataHora": "04/08/2025 11:03:40",
                    "mensagem": "Não é possivel criar o clube pois o estado AX não existe.",
                    "errors": null
                }
                """
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Já existe clube com esse nome no estado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            value = """
                {
                    "codigoErro": "CLUBE_DUPLICADO",
                    "dataHora": "04/08/2025 11:04:33",
                    "mensagem": "Já existe clube com este nome no mesmo estado.",
                    "errors": null
                }
                """
                    )
            )
    )
    @PostMapping
    public ResponseEntity<ClubeResponseDTO> criar(@RequestBody @Valid CriarClubeRequestDTO clubeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(criarClubeService.criarClube(clubeDTO));
    }
}
