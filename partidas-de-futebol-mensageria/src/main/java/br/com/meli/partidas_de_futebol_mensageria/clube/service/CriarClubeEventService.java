package br.com.meli.partidas_de_futebol_mensageria.clube.service;

import br.com.meli.partidas_de_futebol_mensageria.clube.dto.ClubeEvent;
import br.com.meli.partidas_de_futebol_mensageria.clube.dto.CriarClubeRequestDTO;
import br.com.meli.partidas_de_futebol_mensageria.clube.enums.ClubeEventStatus;
import br.com.meli.partidas_de_futebol_mensageria.clube.enums.ClubeEventType;
import br.com.meli.partidas_de_futebol_mensageria.clube.producer.CriarClubeProducer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CriarClubeEventService {
    private final CriarClubeProducer criarClubeProducer;

    public CriarClubeEventService(CriarClubeProducer criarClubeProducer) {
        this.criarClubeProducer = criarClubeProducer;
    }

    public void processarCriacaoClube(CriarClubeRequestDTO dto) {
        ClubeEvent event = new ClubeEvent();
        event.setStatus(ClubeEventStatus.CREATED);
        event.setTipoEvento(ClubeEventType.CRIACAO);
        event.setMessage("Clube enviado para processamento");
        event.setDataHoraEvento(LocalDateTime.now());
        event.setClube(dto);

        criarClubeProducer.sendCriarClube(event);
    }
}
