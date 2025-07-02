package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;

public class EstadioResponseMapper {
    public static EstadioResponseDTO toEstadioResponseDTO(Estadio estadio) {
        EstadioResponseDTO estadioDTO = new EstadioResponseDTO();
        estadioDTO.setNome(estadio.getNome());
        return estadioDTO;
    }
}
