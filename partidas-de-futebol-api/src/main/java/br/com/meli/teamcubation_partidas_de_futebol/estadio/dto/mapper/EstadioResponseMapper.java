package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CepResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioEnderecoResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;

public class EstadioResponseMapper {
    public static EstadioResponseDTO toEstadioResponseDTO(Estadio estadio) {
        return new EstadioResponseDTO(estadio.getNome());
    }
}
