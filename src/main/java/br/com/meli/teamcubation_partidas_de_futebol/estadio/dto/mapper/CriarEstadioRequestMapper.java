package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CriarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;

public class CriarEstadioRequestMapper {
    public static Estadio toEntity(CriarEstadioRequestDTO criarEstadioRequestDTO) {
        Estadio estadio = new Estadio();
        estadio.setNome(criarEstadioRequestDTO.getNome());
        return estadio;
    }
}
