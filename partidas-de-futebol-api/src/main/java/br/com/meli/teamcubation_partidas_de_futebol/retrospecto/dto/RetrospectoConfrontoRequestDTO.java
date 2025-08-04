package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto;

public class RetrospectoConfrontoRequestDTO {
    private final Long clubeId;
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
