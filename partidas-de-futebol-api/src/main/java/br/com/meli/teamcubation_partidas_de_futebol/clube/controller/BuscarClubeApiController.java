package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.ClubeResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
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

@Tag(name = "Clubes", description = "Endpoints para busca, criação, atualização e inativação de clubes")
@RestController
@RequestMapping(value = "/api/clube/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
public class BuscarClubeApiController {
    private final BuscarClubeService buscarClubeService;

    public BuscarClubeApiController(BuscarClubeService buscarClubeService) {
        this.buscarClubeService = buscarClubeService;
    }

    @Operation(
            summary = "Lista clubes filtrando por nome, estado ou ativo",
            description = "Retorna uma página de clubes, permitindo filtro opcional por nome, estado e situação de ativo. Suporta paginação."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista paginada de clubes",
            content = @Content(
                    schema = @Schema(implementation = ClubeResponseDTO.class),
                    examples = {@ExampleObject(
                            name = "lista com resultados",
                            summary = "Retorno de clubes encontrados",
                            value = """
                            {
                        "content": [
                                 {
                                     "nome": "clube de time atualizado",
                                     "siglaEstado": "AM",
                                     "dataCriacao": "2025-05-13"
                                 },
                                 {
                                     "nome": "clube de time",
                                     "siglaEstado": "AP",
                                     "dataCriacao": "2025-05-13"
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
                             "totalElements": 2,
                             "totalPages": 1,
                             "first": true,
                             "size": 20,
                             "number": 0,
                             "sort": {
                                 "empty": true,
                                 "sorted": false,
                                 "unsorted": true
                             },
                             "numberOfElements": 2,
                             "empty": false
                         }
                        """

                    ),
                    @ExampleObject(
                            name = "casoSemResultados",
                            summary = "Retorno quando não encontra clubes para o filtro ou se não há clubes cadastrados.",
                            value = """
                            {
                              "content": [],
                              "pageable": { "pageNumber": 0, "pageSize": 20, ... },
                              "last": true,
                              "totalElements": 0,
                              "totalPages": 0,
                              "first": true,
                              "size": 20,
                              "number": 0,
                              "sort": { "empty": true, "sorted": false, "unsorted": true },
                              "numberOfElements": 0,
                              "empty": true
                            }
                            """
                    )}
            )
    )
    @GetMapping
    public Page<ClubeResponseDTO> listarClubes

            (
            @Parameter(description = "Nome (opcional) para filtro", example = "testando")
            @RequestParam(required = false) String nome,
             @RequestParam(required = false) String estado,
             @RequestParam(required = false) Boolean ativo,
             @Parameter(
                     description = "Campo para ordenação, por padrão já está nome, asc",
                     example = "[\"\"]"
             )
             @PageableDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable){
        return buscarClubeService.listarClubesFiltrados(nome,estado,ativo,pageable);
    }


    @Operation(
            summary = "Busca um clube por ID",
            description = "Retorna os detalhes de um clube localizado pelo seu identificador único."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Clube encontrado com sucesso",
            content = @Content(
                    schema = @Schema(implementation = ClubeResponseDTO.class),
                    examples = @ExampleObject(
                            value = """
            {
              "nome": "clube de time atualizado",
              "siglaEstado": "AM",
              "dataCriacao": "2025-05-13"
            }
            """
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Clube não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class),
                    examples = @ExampleObject(
                            value = """
            {
              "codigoErro": "CLUBE_NAO_ENCONTRADO",
              "dataHora": "04/08/2025 10:23:55",
              "mensagem": "Clube com id 999 não encontrado.",
              "errors": null
            }
            """
                    )
            )
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClubeResponseDTO> buscarPorId(@PathVariable Long id) {
        Clube clubeRetornado = buscarClubeService.buscarClubePorId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubeResponseMapper.toClubeResponseDTO(clubeRetornado));
    }

}
