package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CriarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;

public class CriarEstadioRequestMapper {
    public static Estadio toEntity(CriarEstadioRequestDTO criarEstadioRequestDTO) {
        return new Estadio(
                criarEstadioRequestDTO.getNome(),
                criarEstadioRequestDTO.getCep()
        );
    }
}
