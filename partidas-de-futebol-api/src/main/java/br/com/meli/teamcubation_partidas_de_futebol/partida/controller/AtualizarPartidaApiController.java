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
                    schema = @Schema(implementation = PartidaResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos, clubes iguais, clubes/estádio inexistentes, gols negativos ou data futura",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Clube, estádio ou partida não encontrada",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflitos de regras de negócio: datas, clube inativo, partidas próximas, estádio já ocupado",
            content = @Content(
                    schema = @Schema(implementation = ErroPadrao.class)
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
