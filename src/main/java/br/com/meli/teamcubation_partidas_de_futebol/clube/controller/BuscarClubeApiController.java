package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.ClubeResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clube/buscar")
public class BuscarClubeApiController {
    private final BuscarClubeService buscarClubeService;

    public BuscarClubeApiController(BuscarClubeService buscarClubeService) {
        this.buscarClubeService = buscarClubeService;
    }

    @GetMapping
    public Page<ClubeResponseDTO> listarClubes
            (@RequestParam(required = false) String nome,
             @RequestParam(required = false) String estado,
             @RequestParam(required = false) Boolean ativo,
             Pageable pageable){
        return buscarClubeService.listarClubesFiltrados(nome,estado,ativo,pageable);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> buscarPorId(@PathVariable Long id) {
        Clube clubeRetornado = buscarClubeService.buscarClubePorId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubeResponseMapper.toClubeResponseDTO(clubeRetornado));
    }

}
