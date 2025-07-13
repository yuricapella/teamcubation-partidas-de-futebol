package br.com.meli.teamcubation_partidas_de_futebol.clube.dto;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.RetrospectoContraAdversario;

import java.util.List;

public class RetrospectoAdversariosResponseDTO {
    private final String nomeClube;
    private final String estadoClube;
    private final List<RetrospectoContraAdversario>  retrospectoContraAdversarios;

    public RetrospectoAdversariosResponseDTO(String nomeClube, String estadoClube, List<RetrospectoContraAdversario> retrospectoContraAdversarios) {
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

    public List<RetrospectoContraAdversario> getRetrospectoContraAdversarios() {
        return retrospectoContraAdversarios;
    }
}
