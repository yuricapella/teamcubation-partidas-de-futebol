package br.com.meli.teamcubation_partidas_de_futebol.estadio.controller;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.AtualizarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CriarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper.EstadioResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.AtualizarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.BuscarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.CriarEstadioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estadio")
public class EstadioApiController {
    private final CriarEstadioService criarEstadioService;
    private final BuscarEstadioService buscarEstadioService;
    private final AtualizarEstadioService atualizarEstadioService;

    public EstadioApiController(CriarEstadioService criarEstadioService, BuscarEstadioService buscarEstadioService, AtualizarEstadioService atualizarEstadioService) {
        this.criarEstadioService = criarEstadioService;
        this.buscarEstadioService = buscarEstadioService;
        this.atualizarEstadioService = atualizarEstadioService;
    }

    @PostMapping
    public ResponseEntity<EstadioResponseDTO> criar(@RequestBody @Valid CriarEstadioRequestDTO criarEstadioRequestDTO) {
        Estadio estadioCriado = criarEstadioService.criarEstadio(criarEstadioRequestDTO);
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

    @PutMapping("/{id}")
    public ResponseEntity<EstadioResponseDTO> atualizar
            (@RequestBody AtualizarEstadioRequestDTO atualizarEstadioRequestDTO, @PathVariable Long id) {
        Estadio estadioAtualizado = atualizarEstadioService.AtualizarEstadio(atualizarEstadioRequestDTO, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(EstadioResponseMapper.toEstadioResponseDTO(estadioAtualizado));
    }

    @GetMapping
    public Page<EstadioResponseDTO> buscarTodosEstadiosFiltrados(
            @RequestParam(required = false) String nome, Pageable pageable) {
        return buscarEstadioService.listarEstadiosFiltrados(nome,pageable);
    }
}
