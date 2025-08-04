package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.AtualizarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.AtualizarPartidaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partida/atualizar")
public class AtualizarPartidaApiController {
    private final AtualizarPartidaService atualizarPartidaService;

    public AtualizarPartidaApiController(AtualizarPartidaService atualizarPartidaService) {
        this.atualizarPartidaService = atualizarPartidaService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> atualizarPartidaPorId(@RequestBody @Valid AtualizarPartidaRequestDTO atualizarPartidaRequestDTO, @PathVariable Long id) {
        Partida partidaAtualizada = atualizarPartidaService.atualizarPartida(atualizarPartidaRequestDTO, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PartidaResponseMapper.toPartidaResponseDTO(partidaAtualizada));
    }

}
