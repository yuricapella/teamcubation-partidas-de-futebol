package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.AtualizarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.AtualizarPartidaService;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.BuscarPartidaService;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.CriarPartidaService;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.DeletarPartidaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partida")
public class PartidaApiController {
    private final CriarPartidaService criarPartidaService;
    private final BuscarPartidaService buscarPartidaService;
    private final DeletarPartidaService deletarPartidaService;
    private final AtualizarPartidaService atualizarPartidaService;

    public PartidaApiController(CriarPartidaService criarPartidaService, BuscarPartidaService buscarPartidaService, DeletarPartidaService deletarPartidaService, AtualizarPartidaService atualizarPartidaService) {
        this.criarPartidaService = criarPartidaService;
        this.buscarPartidaService = buscarPartidaService;
        this.deletarPartidaService = deletarPartidaService;
        this.atualizarPartidaService = atualizarPartidaService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPartidaPorId(@PathVariable Long id) {
        deletarPartidaService.deletarPartidaPorId(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> atualizarPartidaPorId(@RequestBody @Valid AtualizarPartidaRequestDTO atualizarPartidaRequestDTO, @PathVariable Long id) {
        Partida partidaAtualizada = atualizarPartidaService.atualizarPartida(atualizarPartidaRequestDTO, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PartidaResponseMapper.toPartidaResponseDTO(partidaAtualizada));
    }

    @GetMapping
    public Page<PartidaResponseDTO> listarPartidas(
            @RequestParam(required = false) Long clubeId,
            @RequestParam(required = false) Long estadioId,
            Pageable pageable
    ) {
        return buscarPartidaService.listarPartidasFiltradas(clubeId, estadioId, pageable)
                .map(PartidaResponseMapper::toPartidaResponseDTO);
    }
}
