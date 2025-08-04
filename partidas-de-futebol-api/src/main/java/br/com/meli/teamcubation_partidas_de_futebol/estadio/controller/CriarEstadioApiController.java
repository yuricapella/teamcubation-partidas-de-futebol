package br.com.meli.teamcubation_partidas_de_futebol.estadio.controller;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CriarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioEnderecoResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.CriarEstadioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estadio/criar")
public class CriarEstadioApiController {
    private final CriarEstadioService criarEstadioService;

    public CriarEstadioApiController(CriarEstadioService criarEstadioService) {
        this.criarEstadioService = criarEstadioService;
    }

    @PostMapping
    public ResponseEntity<EstadioEnderecoResponseDTO> criar(@RequestBody @Valid CriarEstadioRequestDTO criarEstadioRequestDTO) {
        EstadioEnderecoResponseDTO estadioCriado = criarEstadioService.criarEstadio(criarEstadioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(estadioCriado);
    }

}
