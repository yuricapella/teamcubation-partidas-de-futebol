package br.com.meli.teamcubation_partidas_de_futebol.partida.dto;

import java.time.LocalDateTime;

public class CriarPartidaRequestDTO {
    Long clubeMandanteId;
    Long clubeVisitanteId;
    Long estadioId;
    int golsMandante;
    int golsVisitante;
    LocalDateTime dataHora;

    public CriarPartidaRequestDTO(Long clubeMandanteId, Long clubeVisitanteId, Long estadioId, int golsMandante, int golsVisitante, LocalDateTime dataHora) {
        this.clubeMandanteId = clubeMandanteId;
        this.clubeVisitanteId = clubeVisitanteId;
        this.estadioId = estadioId;
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
        this.dataHora = dataHora;
    }

    public Long getClubeMandanteId() {
        return clubeMandanteId;
    }

    public void setClubeMandanteId(Long clubeMandanteId) {
        this.clubeMandanteId = clubeMandanteId;
    }

    public Long getClubeVisitanteId() {
        return clubeVisitanteId;
    }

    public void setClubeVisitanteId(Long clubeVisitanteId) {
        this.clubeVisitanteId = clubeVisitanteId;
    }

    public Long getEstadioId() {
        return estadioId;
    }

    public void setEstadioId(Long estadioId) {
        this.estadioId = estadioId;
    }

    public int getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(int golsMandante) {
        this.golsMandante = golsMandante;
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(int golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
