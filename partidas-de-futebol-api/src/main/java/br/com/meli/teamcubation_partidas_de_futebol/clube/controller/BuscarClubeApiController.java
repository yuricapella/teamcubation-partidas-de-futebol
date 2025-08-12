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
                    examples = @ExampleObject(
                            name = "lista com resultados ou vazia",
                            summary = "Retorno com lista de clubes encontrados ou lista vazia se clubes não existirem"
                    )
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
                    schema = @Schema(implementation = ClubeResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Clube não encontrado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClubeResponseDTO> buscarPorId(@PathVariable Long id) {
        Clube clubeRetornado = buscarClubeService.buscarClubePorId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubeResponseMapper.toClubeResponseDTO(clubeRetornado));
    }

}
