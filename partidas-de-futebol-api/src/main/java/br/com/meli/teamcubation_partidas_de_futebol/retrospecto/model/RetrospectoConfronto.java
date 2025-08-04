package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model;

import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;

import java.util.List;

public class RetrospectoConfronto {
    private final List<Retrospecto>  retrospectos;
    private final List<PartidaResponseDTO>  partidas;

    public RetrospectoConfronto(List<Retrospecto> retrospectos, List<PartidaResponseDTO> partidas) {
        this.retrospectos = retrospectos;
        this.partidas = partidas;
    }

    public List<Retrospecto> getRetrospectos() {
        return retrospectos;
    }

    public List<PartidaResponseDTO> getPartidas() {
        return partidas;
    }
}
