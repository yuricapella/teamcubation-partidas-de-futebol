package br.com.meli.teamcubation_partidas_de_futebol.partida.dto;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.Resultado;
import br.com.meli.teamcubation_partidas_de_futebol.util.FormatadorDeData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(description = "Dados de resposta de uma partida cadastrada ou consultada")
public class PartidaResponseDTO {
    @Schema(description = "Dados do clube mandante da partida")
    private ClubeResponseDTO clubeMandante;

    @Schema(description = "Dados do clube visitante da partida")
    private ClubeResponseDTO clubeVisitante;

    @Schema(description = "Estádio onde a partida ocorreu")
    private EstadioResponseDTO estadio;

    @Schema(description = "Gols marcados pelo mandante", example = "2")
    private int golsMandante;

    @Schema(description = "Gols marcados pelo visitante", example = "1")
    private int golsVisitante;

    @Schema(description = "Data e hora da partida", example = "04/08/2025 18:35:13", type = "string", format = "date-time")
    @JsonFormat(pattern = FormatadorDeData.PADRAO_DATA_HORA)
    private LocalDateTime dataHora;

    @Schema(description = "Resultado da partida: VITORIA_MANDANTE, VITORIA_VISITANTE ou EMPATE", example = "VITORIA_MANDANTE")
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