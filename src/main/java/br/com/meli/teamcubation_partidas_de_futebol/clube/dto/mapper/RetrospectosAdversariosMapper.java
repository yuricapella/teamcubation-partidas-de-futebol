package br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.RetrospectoContraAdversario;

import java.util.List;

public class RetrospectosAdversariosMapper {
    public static RetrospectoAdversariosResponseDTO toDTO(String nomeClube, String estadoClube, List<RetrospectoContraAdversario> retrospectos) {
        return new RetrospectoAdversariosResponseDTO(nomeClube, estadoClube, retrospectos);
    }
}
