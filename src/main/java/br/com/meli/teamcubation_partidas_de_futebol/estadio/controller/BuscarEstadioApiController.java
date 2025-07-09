package br.com.meli.teamcubation_partidas_de_futebol.estadio.controller;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper.EstadioResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.BuscarEstadioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estadio/buscar")
public class BuscarEstadioApiController {
    private final BuscarEstadioService buscarEstadioService;

    public BuscarEstadioApiController(BuscarEstadioService buscarEstadioService) {
        this.buscarEstadioService = buscarEstadioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioResponseDTO> buscar(@PathVariable Long id) {
        Estadio estadioBuscado = buscarEstadioService.buscarEstadioPorId(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(EstadioResponseMapper.toEstadioResponseDTO(estadioBuscado));
    }

    @GetMapping
    public Page<EstadioResponseDTO> buscarTodosEstadiosFiltrados(
            @RequestParam(required = false) String nome, Pageable pageable) {
        return buscarEstadioService.listarEstadiosFiltrados(nome,pageable);
    }
}
