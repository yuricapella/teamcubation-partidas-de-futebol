package br.com.meli.teamcubation_partidas_de_futebol.clube.rabbitmq.dto;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.CriarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.rabbitmq.enums.ClubeEventStatus;
import br.com.meli.teamcubation_partidas_de_futebol.clube.rabbitmq.enums.ClubeEventType;

import java.time.LocalDateTime;

public class ClubeEvent {
    private ClubeEventStatus status;
    private ClubeEventType tipoEvento;
    private String message;
    private LocalDateTime dataHoraEvento;
    private CriarClubeRequestDTO clube;

    public ClubeEvent() {
    }

    public ClubeEvent(ClubeEventStatus status, ClubeEventType tipoEvento, String message, LocalDateTime dataHoraEvento, CriarClubeRequestDTO clube) {
        this.status = status;
        this.tipoEvento = tipoEvento;
        this.message = message;
        this.dataHoraEvento = dataHoraEvento;
        this.clube = clube;
    }

    public ClubeEventStatus getStatus() {
        return status;
    }

    public void setStatus(ClubeEventStatus status) {
        this.status = status;
    }

    public ClubeEventType getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(ClubeEventType tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDataHoraEvento() {
        return dataHoraEvento;
    }

    public void setDataHoraEvento(LocalDateTime dataHoraEvento) {
        this.dataHoraEvento = dataHoraEvento;
    }

    public CriarClubeRequestDTO getClube() {
        return clube;
    }

    public void setClube(CriarClubeRequestDTO clube) {
        this.clube = clube;
    }

    @Override
    public String toString() {
        return "ClubeEvent(status=" + status +
                ", tipoEvento=" + tipoEvento +
                ", message=" + message +
                ", dataHoraEvento=" + dataHoraEvento +
                ", clube=" + clube +
                ")";
    }
}
