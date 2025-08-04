package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.partida.service.DeletarPartidaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partida/deletar")
public class DeletarPartidaApiController {
    private final DeletarPartidaService deletarPartidaService;

    public DeletarPartidaApiController(DeletarPartidaService deletarPartidaService) {
        this.deletarPartidaService = deletarPartidaService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPartidaPorId(@PathVariable Long id) {
        deletarPartidaService.deletarPartidaPorId(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
