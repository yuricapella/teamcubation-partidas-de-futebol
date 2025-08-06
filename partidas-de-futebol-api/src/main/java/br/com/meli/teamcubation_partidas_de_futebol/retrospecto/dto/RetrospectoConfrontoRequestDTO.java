package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para buscar o confronto direto entre dois clubes")
public class RetrospectoConfrontoRequestDTO {
    @Schema(description = "ID do clube principal", example = "1")
    private final Long clubeId;

    @Schema(description = "ID do clube adversário", example = "2")
    private final Long adversarioId;

    public RetrospectoConfrontoRequestDTO(Long clubeId, Long adversarioId) {
        this.clubeId = clubeId;
        this.adversarioId = adversarioId;
    }

    public Long getClubeId() {
        return clubeId;
    }

    public Long getAdversarioId() {
        return adversarioId;
    }
}
