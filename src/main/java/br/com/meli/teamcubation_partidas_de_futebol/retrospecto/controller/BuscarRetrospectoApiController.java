package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.controller;

import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoConfronto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.service.BuscarRetrospectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clube")
public class BuscarRetrospectoApiController {
    private final BuscarRetrospectoService buscarRetrospectoService;

    public BuscarRetrospectoApiController(BuscarRetrospectoService buscarRetrospectoService) {
        this.buscarRetrospectoService = buscarRetrospectoService;
    }

    @GetMapping("/{id}/retrospecto")
    public ResponseEntity<Retrospecto> buscar
            (@PathVariable Long id,
             @RequestParam(required = false) Boolean mandante,
             @RequestParam(required = false) Boolean visitante){
        Retrospecto retrospecto = buscarRetrospectoService.buscarRetrospectoClube(id, mandante, visitante);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(retrospecto);
    }

    @GetMapping("/{id}/retrospectos-adversarios")
    public ResponseEntity<RetrospectoAdversariosResponseDTO> buscarContraAdversarios
            (@PathVariable Long id,
             @RequestParam(required = false) Boolean mandante,
             @RequestParam(required = false) Boolean visitante)
    {
        RetrospectoAdversariosResponseDTO retrospectosDTO =
                buscarRetrospectoService.buscarRetrospectoClubeContraAdversarios(id, mandante, visitante);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(retrospectosDTO);
    }

    @GetMapping("/{idClube}/confronto/{idAdversario}")
    public ResponseEntity<RetrospectoConfronto> buscarRetrospectoContraAdversario(
            @PathVariable Long idClube,
            @PathVariable Long idAdversario
    ) {
        RetrospectoConfronto retrospectoConfronto = buscarRetrospectoService
                .buscarRetrospectoConfronto(idClube, idAdversario);
        return ResponseEntity.status(HttpStatus.OK).body(retrospectoConfronto);
    }
}
