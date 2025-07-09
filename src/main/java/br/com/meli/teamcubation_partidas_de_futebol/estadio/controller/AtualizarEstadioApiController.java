package br.com.meli.teamcubation_partidas_de_futebol.estadio.controller;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.AtualizarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper.EstadioResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.AtualizarEstadioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estadio/atualizar")
public class AtualizarEstadioApiController {
    private final AtualizarEstadioService atualizarEstadioService;

    public AtualizarEstadioApiController(AtualizarEstadioService atualizarEstadioService) {
        this.atualizarEstadioService = atualizarEstadioService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadioResponseDTO> atualizar
            (@RequestBody @Valid AtualizarEstadioRequestDTO atualizarEstadioRequestDTO, @PathVariable Long id) {
        Estadio estadioAtualizado = atualizarEstadioService.atualizarEstadio(atualizarEstadioRequestDTO, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(EstadioResponseMapper.toEstadioResponseDTO(estadioAtualizado));
    }

}
