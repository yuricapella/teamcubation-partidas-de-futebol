package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.CriarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.ClubeResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.CriarClubeRequestMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.CriarClubeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clube")
public class ClubeApiController {
    private final CriarClubeService criarClubeService;

    public ClubeApiController(CriarClubeService criarClubeService) {
        this.criarClubeService = criarClubeService;
    }

    @PostMapping
    public ResponseEntity<ClubeResponseDTO> criar(@RequestBody @Valid CriarClubeRequestDTO clubeDTO) {
        Clube clubeCriado = criarClubeService.criarClube(CriarClubeRequestMapper.toEntity(clubeDTO));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClubeResponseMapper.toClubeResponseDTO(clubeCriado));
    }
}
