package br.com.meli.teamcubation_partidas_de_futebol.partida.dto;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.Resultado;
import br.com.meli.teamcubation_partidas_de_futebol.util.FormatadorDeData;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class PartidaResponseDTO {
    private ClubeResponseDTO clubeMandante;
    private ClubeResponseDTO clubeVisitante;
    private EstadioResponseDTO estadio;
    private int golsMandante;
    private int golsVisitante;
    @JsonFormat(pattern = FormatadorDeData.PADRAO_DATA_HORA)
    private LocalDateTime dataHora;
    private Resultado resultado;

    public PartidaResponseDTO() {
    }

    public PartidaResponseDTO(ClubeResponseDTO clubeMandante, ClubeResponseDTO clubeVisitante, EstadioResponseDTO estadio, int golsMandante, int golsVisitante, LocalDateTime dataHora, Resultado resultado) {
        this.clubeMandante = clubeMandante;
        this.clubeVisitante = clubeVisitante;
        this.estadio = estadio;
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
        this.dataHora = dataHora;
        this.resultado = resultado;
    }

    public ClubeResponseDTO getClubeMandante() {
        return clubeMandante;
    }

    public void setClubeMandante(ClubeResponseDTO clubeMandante) {
        this.clubeMandante = clubeMandante;
    }

    public ClubeResponseDTO getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(ClubeResponseDTO clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
    }

    public EstadioResponseDTO getEstadio() {
        return estadio;
    }

    public void setEstadio(EstadioResponseDTO estadio) {
        this.estadio = estadio;
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

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }
}