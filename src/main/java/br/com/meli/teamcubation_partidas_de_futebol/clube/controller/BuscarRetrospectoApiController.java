package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.RetrospectoClube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarRetrospectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clube/retrospecto")
public class BuscarRetrospectoApiController {
    private final BuscarRetrospectoService buscarRetrospectoService;

    public BuscarRetrospectoApiController(BuscarRetrospectoService buscarRetrospectoService) {
        this.buscarRetrospectoService = buscarRetrospectoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RetrospectoClube> buscar(@PathVariable Long id){
        RetrospectoClube retrospecto = buscarRetrospectoService.buscarRetrospecto(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(retrospecto);
    }
}
