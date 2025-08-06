package br.com.meli.teamcubation_partidas_de_futebol.partida.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

@Schema(description = "Dados para atualizar uma partida já cadastrada")
public class AtualizarPartidaRequestDTO {
    @Schema(description = "ID do clube mandante", example = "1")
    @NotNull(message = "O clube mandante é obrigatório")
    private Long clubeMandanteId;

    @Schema(description = "ID do clube visitante", example = "2")
    @NotNull(message = "O clube visitante é obrigatório")
    private Long clubeVisitanteId;

    @Schema(description = "ID do estádio", example = "10")
    @NotNull(message = "O estádio é obrigatório")
    private Long estadioId;

    @Schema(description = "Gols do mandante (não pode ser negativo)", example = "2")
    @PositiveOrZero(message = "O número de gols do mandante não pode ser negativo")
    private int golsMandante;

    @Schema(description = "Gols do visitante (não pode ser negativo)", example = "1")
    @PositiveOrZero(message = "O número de gols do visitante não pode ser negativo")
    private int golsVisitante;

    @Schema(description = "Data e hora da partida", example = "2025-06-10T21:00:00", type = "string", format = "date-time")
    @NotNull(message = "A data e hora da partida são obrigatórias")
    @PastOrPresent(message = "A data da partida não pode ser futura")
    private LocalDateTime dataHora;

    public AtualizarPartidaRequestDTO(Long clubeMandanteId, Long clubeVisitanteId, Long estadioId, int golsMandante, int golsVisitante, LocalDateTime dataHora) {
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
