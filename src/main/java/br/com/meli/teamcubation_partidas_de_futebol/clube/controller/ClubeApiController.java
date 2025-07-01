package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.AtualizarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.CriarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.ClubeResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.CriarClubeRequestMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.AtualizarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.CriarClubeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clube")
public class ClubeApiController {
    private final CriarClubeService criarClubeService;
    private final BuscarClubeService buscarClubeService;
    private final AtualizarClubeService atualizarClubeService;

    public ClubeApiController(CriarClubeService criarClubeService, BuscarClubeService buscarClubeService, AtualizarClubeService atualizarClubeService) {
        this.criarClubeService = criarClubeService;
        this.buscarClubeService = buscarClubeService;
        this.atualizarClubeService = atualizarClubeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> buscarPorId(@PathVariable Long id) {
        Clube clubeRetornado = buscarClubeService.buscarClubePorId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubeResponseMapper.toClubeResponseDTO(clubeRetornado));
    }

    @PostMapping
    public ResponseEntity<ClubeResponseDTO> criar(@RequestBody @Valid CriarClubeRequestDTO clubeDTO) {
        Clube clubeCriado = criarClubeService.criarClube(CriarClubeRequestMapper.toEntity(clubeDTO));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClubeResponseMapper.toClubeResponseDTO(clubeCriado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> atualizar(@PathVariable Long id,
                                                      @RequestBody @Valid AtualizarClubeRequestDTO clubeAtualizado) {
        Clube clubeAlterado = atualizarClubeService.atualizar(clubeAtualizado, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubeResponseMapper.toClubeResponseDTO(clubeAlterado));
    }
}
