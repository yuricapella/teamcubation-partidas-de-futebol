package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.BuscarPartidaService;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.CriarPartidaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partida")
public class PartidaApiController {
    private final CriarPartidaService criarPartidaService;
    private final BuscarPartidaService buscarPartidaService;

    public PartidaApiController(CriarPartidaService criarPartidaService, BuscarPartidaService buscarPartidaService) {
        this.criarPartidaService = criarPartidaService;
        this.buscarPartidaService = buscarPartidaService;
    }

    @PostMapping
    public ResponseEntity<PartidaResponseDTO> criar(@RequestBody @Valid CriarPartidaRequestDTO criarPartidaRequestDTO) {
        Partida partidaCriada = criarPartidaService.criarPartida(criarPartidaRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PartidaResponseMapper.toPartidaResponseDTO(partidaCriada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> buscarPartidaPorId(@PathVariable Long id) {
        Partida partidaRetornada = buscarPartidaService.buscarPartidaPorId(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PartidaResponseMapper.toPartidaResponseDTO(partidaRetornada));
    }
}
