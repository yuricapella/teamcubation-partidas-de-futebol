package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.AtualizarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.AtualizarPartidaService;
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

@Tag(name = "Partidas")
@RestController
@RequestMapping(value = "/api/partida/atualizar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AtualizarPartidaApiController {
    private final AtualizarPartidaService atualizarPartidaService;

    public AtualizarPartidaApiController(AtualizarPartidaService atualizarPartidaService) {
        this.atualizarPartidaService = atualizarPartidaService;
    }

    @Operation(
            summary = "Atualiza os dados de uma partida",
            description = "Edita partida existente, validando clubes, estádio, gols, datas. 200 OK se sucesso, 400 para campos ou entidades inválidas, 409 para regras de negócio e 404 para elementos não encontrados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Partida atualizada com sucesso",
            content = @Content(
                    schema = @Schema(implementation = PartidaResponseDTO.class),
                    examples = @ExampleObject(
                            value = """
                {
                    "clubeMandante": {
                        "nome": "clube de time atualizado",
                        "siglaEstado": "AM",
                        "dataCriacao": "2025-05-13"
                    },
                    "clubeVisitante": {
                        "nome": "clube de time",
                        "siglaEstado": "AP",
                        "dataCriacao": "2025-05-13"
                    },
                    "estadio": {
                        "nome": "Estadio de time atualizado com cep meli"
                    },
                    "golsMandante": 2,
                    "golsVisitante": 2,
                    "dataHora": "10/06/2025 21:00:00",
                    "resultado": "EMPATE"
                }
                """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos, clubes iguais, clubes/estádio inexistentes, gols negativos ou data futura",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = {
                            @ExampleObject(
                                    name = "campos-invalidos",
                                    summary = "Gols negativos e data futura",
                                    value = """
                    {
                        "codigoErro": "CAMPO_INVALIDO",
                        "dataHora": "04/08/2025 19:14:14",
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
                        "dataHora": "04/08/2025 19:14:29",
                        "mensagem": "Não é possivel criar a partida pois os clubes são iguais.",
                        "errors": null
                    }
                    """
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflitos de regras de negócio: datas, clube inativo, partidas próximas, estádio já ocupado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = {
                            @ExampleObject(
                                    name = "data-antes-criacao-clube",
                                    summary = "Data da partida anterior à criação de clube",
                                    value = """
                    {
                        "codigoErro": "DATA_PARTIDA_ANTERIOR_A_CRIACAO_DO_CLUBE",
                        "dataHora": "04/08/2025 19:14:42",
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
                        "dataHora": "04/08/2025 19:15:02",
                        "mensagem": "Não é possivel criar a partida pois há um clube inativo",
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
                        "dataHora": "04/08/2025 19:17:18",
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
                        "dataHora": "04/08/2025 19:25:18",
                        "mensagem": "Não é possivel criar a partida pois no estádio já tem uma partida marcada para o mesmo dia",
                        "errors": null
                    }
                    """
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Clube, estádio ou partida não encontrada",
            content = @Content(
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
                    "mensagem": "Estadio com id 999 não encontrado.",
                    "errors": null
                }
                """
                            ),
                            @ExampleObject(
                                    name = "partida-nao-encontrada",
                                    summary = "Partida não encontrada",
                                    value = """
                {
                    "codigoErro": "PARTIDA_NAO_ENCONTRADA",
                    "dataHora": "04/08/2025 19:29:10",
                    "mensagem": "Partida com id 999 não encontrada.",
                    "errors": null
                }
                """
                            )
                    }
            )
    )
    @PutMapping(value = "/{id}")
    public ResponseEntity<PartidaResponseDTO> atualizarPartidaPorId(
            @RequestBody @Valid AtualizarPartidaRequestDTO atualizarPartidaRequestDTO,
            @Parameter(description = "ID da partida a ser atualizada", example = "1")
            @PathVariable Long id) {
        Partida partidaAtualizada = atualizarPartidaService.atualizarPartida(atualizarPartidaRequestDTO, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PartidaResponseMapper.toPartidaResponseDTO(partidaAtualizada));
    }

}
