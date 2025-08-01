package br.com.meli.partidas_de_futebol_mensageria.clube.controller;

import br.com.meli.partidas_de_futebol_mensageria.clube.dto.ClubeEvent;
import br.com.meli.partidas_de_futebol_mensageria.clube.dto.CriarClubeRequestDTO;
import br.com.meli.partidas_de_futebol_mensageria.clube.producer.CriarClubeProducer;
import br.com.meli.partidas_de_futebol_mensageria.clube.util.ClubeEventStatus;
import br.com.meli.partidas_de_futebol_mensageria.clube.util.ClubeEventType;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/clube")
public class CriarClubeController {

    private final CriarClubeProducer producer;

    public CriarClubeController(CriarClubeProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/criar")
    public String criarClube(@RequestBody @Valid CriarClubeRequestDTO criarClubeRequestDTO) {
        ClubeEvent event = new ClubeEvent();
        event.setStatus(ClubeEventStatus.CREATED);
        event.setTipoEvento(ClubeEventType.CRIACAO_CLUBE);
        event.setMessage("Clube enviado para processamento");
        event.setDataHoraEvento(LocalDateTime.now());
        event.setClube(criarClubeRequestDTO);

        producer.sendCriarClube(event);
        return "Clube enviado para o RabbitMQ";
    }

}
