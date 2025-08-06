package br.com.meli.teamcubation_partidas_de_futebol.estadio.controller;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioEnderecoResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.BuscarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
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

@Tag(name = "Estádios")
@RestController
@RequestMapping(value = "/api/estadio/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
public class BuscarEstadioApiController {
    private final BuscarEstadioService buscarEstadioService;

    public BuscarEstadioApiController(BuscarEstadioService buscarEstadioService) {
        this.buscarEstadioService = buscarEstadioService;
    }

    @Operation(
            summary = "Busca estádio por id (com endereço via API externa)",
            description = "Retorna os campos completos do estádio, incluindo endereço via integração com VIACEP. 404 se não existe."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Estádio encontrado",
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
            responseCode = "404",
            description = "Estádio não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            value = """
                {
                    "codigoErro": "ESTADIO_NAO_ENCONTRADO",
                    "dataHora": "04/08/2025 16:32:26",
                    "mensagem": "Estadio com id 999 não encontrado.",
                    "errors": null
                }
                """
                    )
            )
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<EstadioEnderecoResponseDTO> buscar(
            @Parameter(description = "ID do estádio a buscar", example = "1")
            @PathVariable Long id) {
        EstadioEnderecoResponseDTO estadioBuscado = buscarEstadioService.buscarEstadioComEnderecoPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(estadioBuscado);
    }

    @Operation(
            summary = "Lista estádios com filtro por nome e paginação",
            description = "Permite listar todos os estádios paginados e filtrar por nome. Retorna 200 com lista vazia se não houver resultados. Os resultados podem ser ordenados pelo campo nome (ascendente/descendente)."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista paginada de estádios",
            content = @Content(
                    schema = @Schema(implementation = EstadioResponseDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "comResultados",
                                    summary = "Exemplo de lista com estádios encontrados",
                                    value = """
                    {
                        "content": [
                            { "nome": "Estadio de time atualizado com cep meli" },
                            { "nome": "Estadio de time atualizado dois" },
                            { "nome": "Estadio de time tres" }
                        ],
                        "pageable": {
                            "pageNumber": 0,
                            "pageSize": 20,
                            "sort": { "empty": true, "sorted": false, "unsorted": true },
                            "offset": 0,
                            "paged": true,
                            "unpaged": false
                        },
                        "last": true,
                        "totalPages": 1,
                        "totalElements": 11,
                        "first": true,
                        "size": 20,
                        "number": 0,
                        "sort": { "empty": true, "sorted": false, "unsorted": true },
                        "numberOfElements": 11,
                        "empty": false
                    }
                    """
                            ),
                            @ExampleObject(
                                    name = "vazio",
                                    summary = "Exemplo de lista vazia (sem resultados para o filtro)",
                                    value = """
                    {
                        "content": [],
                        "pageable": {
                            "pageNumber": 0,
                            "pageSize": 20,
                            "sort": { "empty": true, "sorted": false, "unsorted": true },
                            "offset": 0,
                            "paged": true,
                            "unpaged": false
                        },
                        "last": true,
                        "totalPages": 0,
                        "totalElements": 0,
                        "first": true,
                        "size": 20,
                        "number": 0,
                        "sort": { "empty": true, "sorted": false, "unsorted": true },
                        "numberOfElements": 0,
                        "empty": true
                    }
                    """
                            )
                    }
            )
    )
    @GetMapping
    public Page<EstadioResponseDTO> buscarTodosEstadiosFiltrados(
            @Parameter(description = "Nome (opcional) para filtro", example = "Arena")
            @RequestParam(required = false) String nome,
            @Parameter(
                    description = "Campo para ordenação, por padrão já está nome, asc",
                    example = "[\"\"]"
            )
            @PageableDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return buscarEstadioService.listarEstadiosFiltrados(nome,pageable);
    }
}
