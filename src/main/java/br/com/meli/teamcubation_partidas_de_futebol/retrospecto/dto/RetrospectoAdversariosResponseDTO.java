package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto;

import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoAdversario;

import java.util.List;

public class RetrospectoAdversariosResponseDTO {
    private final String nomeClube;
    private final String estadoClube;
    private final List<RetrospectoAdversario>  retrospectoContraAdversarios;

    public RetrospectoAdversariosResponseDTO(String nomeClube, String estadoClube, List<RetrospectoAdversario> retrospectoContraAdversarios) {
        this.nomeClube = nomeClube;
        this.estadoClube = estadoClube;
        this.retrospectoContraAdversarios = retrospectoContraAdversarios;
    }

    public String getNomeClube() {
        return nomeClube;
    }

    public String getEstadoClube() {
        return estadoClube;
    }

    public List<RetrospectoAdversario> getRetrospectoContraAdversarios() {
        return retrospectoContraAdversarios;
    }
}
