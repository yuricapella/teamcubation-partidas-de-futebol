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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Estádios")
@RestController
@RequestMapping("/api/estadio/criar")
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
                    mediaType = "application/json",
                    schema = @Schema(implementation = EstadioEnderecoResponseDTO.class),
                    examples = @ExampleObject(
                            value = """
                {
                    "nome": "Arena Central",
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
                    mediaType = "application/json",
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
                    mediaType = "application/json",
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
    @PostMapping
    public ResponseEntity<EstadioEnderecoResponseDTO> criar(@RequestBody @Valid CriarEstadioRequestDTO criarEstadioRequestDTO) {
        EstadioEnderecoResponseDTO estadioCriado = criarEstadioService.criarEstadio(criarEstadioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(estadioCriado);
    }

}
