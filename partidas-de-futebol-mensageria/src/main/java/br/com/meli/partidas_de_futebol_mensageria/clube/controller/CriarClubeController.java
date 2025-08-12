package br.com.meli.partidas_de_futebol_mensageria.clube.controller;

import br.com.meli.partidas_de_futebol_mensageria.clube.dto.CriarClubeRequestDTO;
import br.com.meli.partidas_de_futebol_mensageria.clube.service.CriarClubeEventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clube")
public class CriarClubeController {

    private final CriarClubeEventService eventService;

    public CriarClubeController(CriarClubeEventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/criar")
    public ResponseEntity<String> criarClube(@RequestBody @Valid CriarClubeRequestDTO criarClubeRequestDTO) {
        eventService.processarCriacaoClube(criarClubeRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Clube enviado para o RabbitMQ");
    }

}
