package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.CriarPartidaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partida/criar")
public class CriarPartidaApiController {
    private final CriarPartidaService criarPartidaService;

    public CriarPartidaApiController(CriarPartidaService criarPartidaService) {
        this.criarPartidaService = criarPartidaService;
    }

    @PostMapping
    public ResponseEntity<PartidaResponseDTO> criar(@RequestBody @Valid CriarPartidaRequestDTO criarPartidaRequestDTO) {
        Partida partidaCriada = criarPartidaService.criarPartida(criarPartidaRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PartidaResponseMapper.toPartidaResponseDTO(partidaCriada));
    }

}
