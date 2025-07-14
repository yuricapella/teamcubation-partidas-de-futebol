package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.BuscarPartidaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partida/buscar")
public class BuscarPartidaApiController {
    private final BuscarPartidaService buscarPartidaService;

    public BuscarPartidaApiController(BuscarPartidaService buscarPartidaService) {
        this.buscarPartidaService = buscarPartidaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> buscarPartidaPorId(@PathVariable Long id) {
        Partida partidaRetornada = buscarPartidaService.buscarPartidaPorId(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PartidaResponseMapper.toPartidaResponseDTO(partidaRetornada));
    }

    @GetMapping
    public Page<PartidaResponseDTO> listarPartidas(
            @RequestParam(required = false) Long clubeId,
            @RequestParam(required = false) Long estadioId,
            @RequestParam(required = false) Boolean goleada,
            Pageable pageable
    ) {
        return buscarPartidaService.listarPartidasFiltradas(clubeId, estadioId, goleada, pageable)
                .map(PartidaResponseMapper::toPartidaResponseDTO);
    }
}
