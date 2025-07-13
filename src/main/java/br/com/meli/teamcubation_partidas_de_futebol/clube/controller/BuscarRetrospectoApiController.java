package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.RetrospectoTotalClube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarRetrospectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clube")
public class BuscarRetrospectoApiController {
    private final BuscarRetrospectoService buscarRetrospectoService;

    public BuscarRetrospectoApiController(BuscarRetrospectoService buscarRetrospectoService) {
        this.buscarRetrospectoService = buscarRetrospectoService;
    }

    @GetMapping("/{id}/retrospecto")
    public ResponseEntity<RetrospectoTotalClube> buscar(@PathVariable Long id){
        RetrospectoTotalClube retrospecto = buscarRetrospectoService.buscarRetrospectoClube(id);
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

//    @GetMapping("/adversario")
//    public ResponseEntity<RetrospectoTotalClube> buscarRetrospectoContraAdversario
//            (@RequestBody @Valid RetrospectoContraAdversarioRequestDTO retrospectoContraAdversarioDTO){
//        RetrospectoTotalClube retrospectoContraAdversario = buscarRetrospectoService
//                .buscarRetrospectoClubeContraAdversario(retrospectoContraAdversarioDTO);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(retrospectoContraAdversario);
//    }
}
