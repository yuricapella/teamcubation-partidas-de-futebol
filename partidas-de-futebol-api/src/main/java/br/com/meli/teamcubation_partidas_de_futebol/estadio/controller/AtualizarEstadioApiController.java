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
                    schema = @Schema(implementation = EstadioEnderecoResponseDTO.class),
                    examples = @ExampleObject(
                            value = """
                {
                    "nome": "Estadio de time atualizado com cep meli",
                    "endereco": {
                        "cep": "88032-005",
                        "logradouro": "Rodovia José Carlos Daux",
                        "bairro": "Saco Grande",
                        "localidade": "Florianópolis",
                        "uf": "SC",
                        "estado": "Santa Catarina"
                    }
                }
                """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Campos inválidos",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = {
                            @ExampleObject(
                                    name = "nomeInvalidoECepNulo",
                                    summary = "Nome com menos de 3 letras, CEP nulo",
                                    value = """
                    {
                        "codigoErro": "CAMPO_INVALIDO",
                        "dataHora": "04/08/2025 16:17:13",
                        "mensagem": "Invalid request content.",
                        "errors": {
                            "nome": "O nome tem que ter no minimo três letras",
                            "cep": "não deve ser nulo"
                        }
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "nomeInvalidoECepInvalido",
                                    summary = "Nome com menos de 3 letras, CEP formato inválido",
                                    value = """
                    {
                        "codigoErro": "CAMPO_INVALIDO",
                        "dataHora": "04/08/2025 16:18:31",
                        "mensagem": "Invalid request content.",
                        "errors": {
                            "nome": "O nome tem que ter no minimo três letras",
                            "cep": "O cep deve conter exatamente 8 dígitos numéricos"
                        }
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "nomeNuloECepInvalido",
                                    summary = "Nome nulo, CEP formato inválido",
                                    value = """
                    {
                        "codigoErro": "CAMPO_INVALIDO",
                        "dataHora": "04/08/2025 16:18:54",
                        "mensagem": "Invalid request content.",
                        "errors": {
                            "nome": "não deve ser nulo",
                            "cep": "O cep deve conter exatamente 8 dígitos numéricos"
                        }
                    }
                    """
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Estádio com nome duplicado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            value = """
                {
                    "codigoErro": "ESTADIO_JA_EXISTE",
                    "dataHora": "04/08/2025 16:15:10",
                    "mensagem": "Já existe um estadio com este nome.",
                    "errors": null
                }
                """
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Estádio não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            value = """
                {
                    "codigoErro": "ESTADIO_NAO_ENCONTRADO",
                    "dataHora": "04/08/2025 16:14:43",
                    "mensagem": "Estadio com id 999 não encontrado.",
                    "errors": null
                }
                """
                    )
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
