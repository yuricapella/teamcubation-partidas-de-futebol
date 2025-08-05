package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.CriarPartidaService;
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

@Tag(name = "Partidas")
@RestController
@RequestMapping("/api/partida/criar")
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
                    mediaType = "application/json",
                    schema = @Schema(implementation = PartidaResponseDTO.class),
                    examples = @ExampleObject(
                            value = """
                {
                    "clubeMandante": {
                        "nome": "clube de time criado",
                        "siglaEstado": "AM",
                        "dataCriacao": "2025-05-13"
                    },
                    "clubeVisitante": {
                        "nome": "clube de time dois",
                        "siglaEstado": "AP",
                        "dataCriacao": "2025-05-13"
                    },
                    "estadio": {
                        "nome": "Estadio Exemplo"
                    },
                    "golsMandante": 2,
                    "golsVisitante": 1,
                    "dataHora": "10/06/2025 21:00:00",
                    "resultado": "VITORIA_MANDANTE"
                }
                """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos (campos obrigatórios, gols negativos, data futura, clubes iguais)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = {
                            @ExampleObject(
                                    name = "campos-invalidos",
                                    summary = "Campos obrigatórios não informados ou inválidos",
                                    value = """
                    {
                        "codigoErro": "CAMPO_INVALIDO",
                        "dataHora": "04/08/2025 22:31:14",
                        "mensagem": "Invalid request content.",
                        "errors": {
                            "golsMandante": "O número de gols do mandante não pode ser negativo",
                            "golsVisitante": "O número de gols do visitante não pode ser negativo",
                            "dataHora": "A data da partida não pode ser futura"
                        }
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "clubes-iguais",
                                    summary = "Clubes mandante e visitante iguais",
                                    value = """
                    {
                        "codigoErro": "CLUBES_IGUAIS",
                        "dataHora": "04/08/2025 22:32:29",
                        "mensagem": "Não é possível criar partida pois os clubes são iguais.",
                        "errors": null
                    }
                    """
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflitos de negócio: data anterior à criação de clube, clube inativo, partidas próximas, estádio já ocupado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = {
                            @ExampleObject(
                                    name = "data-antes-criacao-clube",
                                    summary = "Data da partida anterior à criação de clube",
                                    value = """
                    {
                        "codigoErro": "DATA_PARTIDA_ANTERIOR_A_CRIACAO_DO_CLUBE",
                        "dataHora": "04/08/2025 22:37:22",
                        "mensagem": "Não pode cadastrar uma partida para uma data anterior à data de criação do clube.",
                        "errors": null
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "clube-inativo",
                                    summary = "Clube envolvido está inativo",
                                    value = """
                    {
                        "codigoErro": "CLUBE_INATIVO",
                        "dataHora": "04/08/2025 22:38:08",
                        "mensagem": "Não é possível criar a partida pois há um clube inativo.",
                        "errors": null
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "partidas-menor-48h",
                                    summary = "Clube com outras partidas em menos de 48h",
                                    value = """
                    {
                        "codigoErro": "CLUBE_TEM_PARTIDAS_COM_DATA_MENOR_QUE_48_HORAS_DA_NOVA_PARTIDA",
                        "dataHora": "04/08/2025 22:39:10",
                        "mensagem": "Não é possível criar a partida pois um dos clubes já possui uma partida cadastrada em menos de 48 horas desta data.",
                        "errors": null
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "estadio-ja-tem-partida-no-dia",
                                    summary = "Estádio já possui partida marcada para o mesmo dia",
                                    value = """
                    {
                        "codigoErro": "ESTADIO_JA_POSSUI_PARTIDA_MARCADA_NO_MESMO_DIA",
                        "dataHora": "04/08/2025 22:40:24",
                        "mensagem": "Não é possível criar a partida pois no estádio já tem uma partida marcada para o mesmo dia.",
                        "errors": null
                    }
                    """
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Clube ou estádio não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = {
                            @ExampleObject(
                                    name = "clube-nao-encontrado",
                                    summary = "Clube não encontrado",
                                    value = """
                    {
                        "codigoErro": "CLUBE_NAO_ENCONTRADO",
                        "dataHora": "04/08/2025 19:27:34",
                        "mensagem": "Clube com id 999 não encontrado.",
                        "errors": null
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "estadio-nao-encontrado",
                                    summary = "Estádio não encontrado",
                                    value = """
                    {
                        "codigoErro": "ESTADIO_NAO_ENCONTRADO",
                        "dataHora": "04/08/2025 19:28:03",
                        "mensagem": "Estádio com id 999 não encontrado.",
                        "errors": null
                    }
                    """
                            )
                    }
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
