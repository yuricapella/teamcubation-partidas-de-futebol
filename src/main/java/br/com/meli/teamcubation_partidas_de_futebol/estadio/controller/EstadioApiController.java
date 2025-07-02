package br.com.meli.teamcubation_partidas_de_futebol.estadio.controller;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CriarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper.CriarEstadioRequestMapper;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper.EstadioResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.BuscarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.CriarEstadioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estadio")
public class EstadioApiController {
    CriarEstadioService criarEstadioService;
    BuscarEstadioService buscarEstadioService;

    public EstadioApiController(CriarEstadioService criarEstadioService, BuscarEstadioService buscarEstadioService) {
        this.criarEstadioService = criarEstadioService;
        this.buscarEstadioService = buscarEstadioService;
    }

    @PostMapping
    public ResponseEntity<EstadioResponseDTO> criar(@RequestBody @Valid CriarEstadioRequestDTO criarEstadioRequestDTO) {
        Estadio estadioCriado = criarEstadioService.criarEstadio(CriarEstadioRequestMapper.toEntity(criarEstadioRequestDTO));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(EstadioResponseMapper.toEstadioResponseDTO(estadioCriado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioResponseDTO> buscar(@PathVariable Long id) {
        Estadio estadioBuscado = buscarEstadioService.buscarEstadioPorId(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(EstadioResponseMapper.toEstadioResponseDTO(estadioBuscado));
    }
}
