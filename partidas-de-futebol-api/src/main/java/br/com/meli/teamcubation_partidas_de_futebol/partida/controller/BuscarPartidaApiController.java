package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.BuscarPartidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Partidas")
@RestController
@RequestMapping(value = "/api/partida/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
public class BuscarPartidaApiController {
    private final BuscarPartidaService buscarPartidaService;

    public BuscarPartidaApiController(BuscarPartidaService buscarPartidaService) {
        this.buscarPartidaService = buscarPartidaService;
    }

    @Operation(
            summary = "Busca partida por ID",
            description = "Busca uma partida específica pelo ID. Retorna 200 se encontrada, 404 se não existe."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Partida encontrada com sucesso",
            content = @Content(
                    schema = @Schema(implementation = PartidaResponseDTO.class),
                    examples = @ExampleObject(
                            value = """
                {
                    "clubeMandante": {
                        "nome": "Clube Mandante",
                        "siglaEstado": "AM",
                        "dataCriacao": "2025-05-13"
                    },
                    "clubeVisitante": {
                        "nome": "Clube Visitante",
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
            responseCode = "404",
            description = "Partida não encontrada",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            value = """
                {
                    "codigoErro": "PARTIDA_NAO_ENCONTRADA",
                    "dataHora": "04/08/2025 23:30:00",
                    "mensagem": "Partida com id 999 não encontrada.",
                    "errors": null
                }
                """
                    )
            )
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<PartidaResponseDTO> buscarPartidaPorId(@PathVariable Long id) {
        Partida partidaRetornada = buscarPartidaService.buscarPartidaPorId(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PartidaResponseMapper.toPartidaResponseDTO(partidaRetornada));
    }

    @Operation(
            summary = "Lista partidas por filtros",
            description = "Retorna página de partidas filtrando por clubeId, estadioId, goleada, mandante, visitante. Suporta paginação."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista paginada de partidas",
            content = @Content(
                    schema = @Schema(implementation = PartidaResponseDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "lista-com-resultados",
                                    summary = "Partidas encontradas",
                                    value = """
                    {
                        "content": [
                            {
                                "clubeMandante": {
                                    "nome": "Clube Mandante",
                                    "siglaEstado": "AM",
                                    "dataCriacao": "2025-05-13"
                                },
                                "clubeVisitante": {
                                    "nome": "Clube Visitante",
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
                        ],
                        "pageable": {
                            "pageNumber": 0,
                            "pageSize": 20,
                            "sort": {
                                "empty": true,
                                "sorted": false,
                                "unsorted": true
                            },
                            "offset": 0,
                            "paged": true,
                            "unpaged": false
                        },
                        "last": true,
                        "totalElements": 1,
                        "totalPages": 1,
                        "first": true,
                        "size": 20,
                        "number": 0,
                        "sort": {
                            "empty": true,
                            "sorted": false,
                            "unsorted": true
                        },
                        "numberOfElements": 1,
                        "empty": false
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "lista-vazia",
                                    summary = "Nenhuma partida encontrada para os filtros",
                                    value = """
                    {
                        "content": [],
                        "pageable": {
                            "pageNumber": 0,
                            "pageSize": 20,
                            "sort": {
                                "empty": true,
                                "sorted": false,
                                "unsorted": true
                            },
                            "offset": 0,
                            "paged": true,
                            "unpaged": false
                        },
                        "last": true,
                        "totalElements": 0,
                        "totalPages": 0,
                        "first": true,
                        "size": 20,
                        "number": 0,
                        "sort": {
                            "empty": true,
                            "sorted": false,
                            "unsorted": true
                        },
                        "numberOfElements": 0,
                        "empty": true
                    }
                    """
                            )
                    }
            )
    )
    @GetMapping
    public Page<PartidaResponseDTO> listarPartidas(
            @Parameter(description = "ID do clube para filtragem", example = "1")
            @RequestParam(required = false) Long clubeId,
            @Parameter(description = "ID do estádio para filtragem", example = "1")
            @RequestParam(required = false) Long estadioId,
            @Parameter(description = "Filtrar apenas partidas consideradas goleada", example = "true")
            @RequestParam(required = false) Boolean goleada,
            @RequestParam(required = false) Boolean mandante,
            @RequestParam(required = false) Boolean visitante,
            @Parameter(
                    description = "Campo para ordenação, por padrão já está dataHora, asc",
                    example = "[\"\"]"
            )
            @PageableDefault(sort = "dataHora", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return buscarPartidaService.listarPartidasFiltradas(clubeId, estadioId, goleada, mandante, visitante, pageable)
                .map(PartidaResponseMapper::toPartidaResponseDTO);
    }
}
