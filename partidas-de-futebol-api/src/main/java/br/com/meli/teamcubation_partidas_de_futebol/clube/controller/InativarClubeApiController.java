package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.service.InativarClubeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clube/inativar")
public class InativarClubeApiController {
    private final InativarClubeService inativarClubeService;

    public InativarClubeApiController(InativarClubeService inativarClubeService) {
        this.inativarClubeService = inativarClubeService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        inativarClubeService.inativarClubePorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
