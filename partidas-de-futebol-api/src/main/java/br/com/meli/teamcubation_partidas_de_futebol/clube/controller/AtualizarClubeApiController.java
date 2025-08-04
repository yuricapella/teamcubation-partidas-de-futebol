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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clubes")
@RestController
@RequestMapping("/api/clube/atualizar")
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
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClubeResponseDTO.class),
                    examples = @ExampleObject(
                            value = """
                {
                  "nome":"Clube Atualizado",
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
                                    summary = "Tentativa de atualizar clube com estado que não existe",
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
            description = "Conflito de dados (data inválida ou duplicidade de clube)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = {
                            @ExampleObject(
                                    name = "data-criacao-posterior",
                                    summary = "Data de criação após uma partida cadastrada",
                                    value = """
                    {
                        "codigoErro": "DATA_CRIACAO_POSTERIOR_A_DATA_PARTIDA",
                        "dataHora": "04/08/2025 11:52:08",
                        "mensagem": "A data de criação do clube com id 1 está posterior a data de uma partida cadastrada.",
                        "errors": null
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "clube-duplicado",
                                    summary = "Nome duplicado para o mesmo estado",
                                    value = """
                    {
                        "codigoErro": "CLUBE_DUPLICADO",
                        "dataHora": "04/08/2025 11:04:33",
                        "mensagem": "Já existe clube com este nome no mesmo estado.",
                        "errors": null
                    }
                    """
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Clube não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            name = "clube-nao-encontrado",
                            summary = "Clube não existe na base de dados",
                            value = """
                {
                    "codigoErro": "CLUBE_NAO_ENCONTRADO",
                    "dataHora": "04/08/2025 11:52:23",
                    "mensagem": "Clube com id 999 não encontrado.",
                    "errors": null
                }
                """
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
