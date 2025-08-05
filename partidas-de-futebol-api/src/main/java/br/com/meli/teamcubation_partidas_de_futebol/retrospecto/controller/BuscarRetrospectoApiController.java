package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.controller;

import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoConfronto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.service.BuscarRetrospectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Retrospecto")
@RestController
@RequestMapping("/api/clube")
public class BuscarRetrospectoApiController {
    private final BuscarRetrospectoService buscarRetrospectoService;

    public BuscarRetrospectoApiController(BuscarRetrospectoService buscarRetrospectoService) {
        this.buscarRetrospectoService = buscarRetrospectoService;
    }

    @Operation(
            summary = "Busca o retrospecto geral de um clube",
            description = "Retorna dados agregados de jogos, vitórias, empates, derrotas, gols feitos e sofridos. Retorna campos zerados se não houver partidas. 404 se clube não existe."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Retrospecto retornado com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Retrospecto.class),
                    examples = {
                            @ExampleObject(
                                    name = "com-jogos",
                                    summary = "Clube com partidas registradas",
                                    value = """
                    {
                        "clube": {
                            "nome": "clube de time atualizado",
                            "siglaEstado": "AM",
                            "dataCriacao": "2025-05-13"
                        },
                        "jogos": 4,
                        "vitorias": 1,
                        "derrotas": 2,
                        "empates": 1,
                        "golsFeitos": 6,
                        "golsSofridos": 11
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "sem-jogos",
                                    summary = "Clube sem partidas (todos campos zerados)",
                                    value = """
                    {
                        "clube": {
                            "nome": "clube de time quatro atualizado",
                            "siglaEstado": "SP",
                            "dataCriacao": "2025-05-13"
                        },
                        "jogos": 0,
                        "vitorias": 0,
                        "derrotas": 0,
                        "empates": 0,
                        "golsFeitos": 0,
                        "golsSofridos": 0
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
                            value = """
                    {
                        "codigoErro": "CLUBE_NAO_ENCONTRADO",
                        "dataHora": "05/08/2025 14:41:46",
                        "mensagem": "Clube com id 999 não encontrado.",
                        "errors": null
                    }
                    """
                    )
            )
    )
    @GetMapping("/{id}/retrospecto")
    public ResponseEntity<Retrospecto> buscarRetrospectoTotalDeUmClube
            (@Parameter(description = "ID do clube", example = "1")
             @PathVariable Long id,
             @Parameter(description = "Filtrar só partidas como mandante", example = "true")
             @RequestParam(required = false) Boolean mandante,
             @Parameter(description = "Filtrar só partidas como visitante", example = "false")
             @RequestParam(required = false) Boolean visitante){
        Retrospecto retrospecto = buscarRetrospectoService.buscarRetrospectoClube(id, mandante, visitante);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(retrospecto);
    }

    @Operation(
            summary = "Busca retrospecto de um clube contra todos adversários",
            description = "Mostra resumo de partidas, vitórias, empates, derrotas, gols feitos/sofridos por adversário. Retorna lista vazia se não houver confrontos. 404 caso o clube não exista."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Retrospecto contra adversários retornado com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RetrospectoAdversariosResponseDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "com-adversarios",
                                    summary = "Lista de retrospectos para adversários",
                                    value = """
                    {
                        "nomeClube": "clube de time atualizado",
                        "estadoClube": "AM",
                        "retrospectoContraAdversarios": [
                            {
                                "nomeAdversario": "aclube de time",
                                "estadoAdversario": "AP",
                                "jogos": 1,
                                "vitorias": 0,
                                "derrotas": 1,
                                "empates": 0,
                                "golsFeitos": 1,
                                "golsSofridos": 4
                            },
                            {
                                "nomeAdversario": "clube de time",
                                "estadoAdversario": "AP",
                                "jogos": 2,
                                "vitorias": 1,
                                "derrotas": 0,
                                "empates": 1,
                                "golsFeitos": 4,
                                "golsSofridos": 3
                            },
                            {
                                "nomeAdversario": "time teste",
                                "estadoAdversario": "SP",
                                "jogos": 1,
                                "vitorias": 0,
                                "derrotas": 1,
                                "empates": 0,
                                "golsFeitos": 1,
                                "golsSofridos": 4
                            }
                        ]
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "sem-adversarios",
                                    summary = "Clube sem confrontos (lista vazia)",
                                    value = """
                    {
                        "nomeClube": "clube de time quatro atualizado",
                        "estadoClube": "SP",
                        "retrospectoContraAdversarios": []
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
                            value = """
                    {
                        "codigoErro": "CLUBE_NAO_ENCONTRADO",
                        "dataHora": "05/08/2025 14:51:33",
                        "mensagem": "Clube com id 999 não encontrado.",
                        "errors": null
                    }
                    """
                    )
            )
    )
    @GetMapping("/{id}/retrospectos-adversarios")
    public ResponseEntity<RetrospectoAdversariosResponseDTO> buscarContraAdversarios
            (@Parameter(description = "ID do clube", example = "1")
             @PathVariable Long id,
             @Parameter(description = "Filtrar só partidas como mandante", example = "true")
             @RequestParam(required = false) Boolean mandante,
             @Parameter(description = "Filtrar só partidas como visitante", example = "false")
             @RequestParam(required = false) Boolean visitante)
    {
        RetrospectoAdversariosResponseDTO retrospectosDTO =
                buscarRetrospectoService.buscarRetrospectoClubeContraAdversarios(id, mandante, visitante);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(retrospectosDTO);
    }

    @Operation(
            summary = "Busca retrospecto de confrontos diretos entre dois clubes",
            description = "Retorna retrospecto consolidado e lista de partidas entre dois clubes. Lista vazia e campos zerados se sem confrontos. 404 caso qualquer clube não exista."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Confronto encontrado ou ambos clubes com retrospecto zerado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RetrospectoConfronto.class),
                    examples = {
                            @ExampleObject(
                                    name = "com-partidas",
                                    summary = "Retrospecto com confrontos",
                                    value = """
        {
            "retrospectos": [
                {
                    "clube": {
                        "nome": "clube de time atualizado",
                        "siglaEstado": "AM",
                        "dataCriacao": "2025-05-13"
                    },
                    "jogos": 2,
                    "vitorias": 1,
                    "derrotas": 0,
                    "empates": 1,
                    "golsFeitos": 4,
                    "golsSofridos": 3
                },
                {
                    "clube": {
                        "nome": "clube de time",
                        "siglaEstado": "AP",
                        "dataCriacao": "2025-05-13"
                    },
                    "jogos": 2,
                    "vitorias": 0,
                    "derrotas": 1,
                    "empates": 1,
                    "golsFeitos": 3,
                    "golsSofridos": 4
                }
            ],
            "partidas": [
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
                    "golsMandante": 1,
                    "golsVisitante": 1,
                    "dataHora": "04/08/2025 18:35:13",
                    "resultado": "EMPATE"
                },
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
                    "golsMandante": 3,
                    "golsVisitante": 2,
                    "dataHora": "05/06/2025 21:00:00",
                    "resultado": "VITORIA_MANDANTE"
                }
            ]
        }
        """
                            ),
                            @ExampleObject(
                                    name = "sem-partidas",
                                    summary = "Não existe confronto, ambos clubes e retrospectos zerados",
                                    value = """
        {
            "retrospectos": [
                {
                    "clube": {
                        "nome": "clube de time atualizado",
                        "siglaEstado": "AM",
                        "dataCriacao": "2025-05-13"
                    },
                    "jogos": 0,
                    "vitorias": 0,
                    "derrotas": 0,
                    "empates": 0,
                    "golsFeitos": 0,
                    "golsSofridos": 0
                },
                {
                    "clube": {
                        "nome": "clube de time quatro atualizado",
                        "siglaEstado": "SP",
                        "dataCriacao": "2025-05-13"
                    },
                    "jogos": 0,
                    "vitorias": 0,
                    "derrotas": 0,
                    "empates": 0,
                    "golsFeitos": 0,
                    "golsSofridos": 0
                }
            ],
            "partidas": []
        }
        """
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Um dos clubes não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            value = """
                    {
                        "codigoErro": "CLUBE_NAO_ENCONTRADO",
                        "dataHora": "05/08/2025 14:57:51",
                        "mensagem": "Clube com id 999 não encontrado.",
                        "errors": null
                    }
                    """
                    )
            )
    )
    @GetMapping("/{idClube}/confronto/{idAdversario}")
    public ResponseEntity<RetrospectoConfronto> buscarConfronto(
            @Parameter(description = "ID do clube principal", example = "1")
            @PathVariable Long idClube,
            @Parameter(description = "ID do adversário", example = "2")
            @PathVariable Long idAdversario
    ) {
        RetrospectoConfronto retrospectoConfronto = buscarRetrospectoService
                .buscarRetrospectoConfronto(idClube, idAdversario);
        return ResponseEntity.status(HttpStatus.OK).body(retrospectoConfronto);
    }
}
