package br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoAdversario;

import java.util.List;

public class RetrospectosAdversariosMapper {
    public static RetrospectoAdversariosResponseDTO toDTO(String nomeClube, String estadoClube, List<RetrospectoAdversario> retrospectos) {
        return new RetrospectoAdversariosResponseDTO(nomeClube, estadoClube, retrospectos);
    }
}
