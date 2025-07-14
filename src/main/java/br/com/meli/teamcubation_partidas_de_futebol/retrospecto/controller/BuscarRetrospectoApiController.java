package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.controller;

import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoConfrontoRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoConfronto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.service.BuscarRetrospectoService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Retrospecto> buscar(@PathVariable Long id){
        Retrospecto retrospecto = buscarRetrospectoService.buscarRetrospectoClube(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(retrospecto);
    }

    @GetMapping("/{id}/retrospectos-adversarios")
    public ResponseEntity<RetrospectoAdversariosResponseDTO> buscarContraAdversarios(@PathVariable Long id){
        RetrospectoAdversariosResponseDTO retrospectosDTO = buscarRetrospectoService.buscarRetrospectoClubeContraAdversarios(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(retrospectosDTO);
    }

    @GetMapping("/confronto")
    public ResponseEntity<RetrospectoConfronto> buscarRetrospectoContraAdversario
            (@RequestBody @Valid RetrospectoConfrontoRequestDTO dto){
        RetrospectoConfronto retrospectoConfronto = buscarRetrospectoService
                .buscarRetrospectoConfronto(dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(retrospectoConfronto);
    }
}
