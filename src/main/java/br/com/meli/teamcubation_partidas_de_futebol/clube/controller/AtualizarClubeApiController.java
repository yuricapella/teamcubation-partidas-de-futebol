package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.AtualizarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.ClubeResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.AtualizarClubeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clube/atualizar")
public class AtualizarClubeApiController {
    private final AtualizarClubeService atualizarClubeService;

    public AtualizarClubeApiController(AtualizarClubeService atualizarClubeService) {
        this.atualizarClubeService = atualizarClubeService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> atualizar(@PathVariable Long id,
                                                      @RequestBody @Valid AtualizarClubeRequestDTO clubeAtualizado) {
        Clube clubeAlterado = atualizarClubeService.atualizar(clubeAtualizado, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubeResponseMapper.toClubeResponseDTO(clubeAlterado));
    }

}
