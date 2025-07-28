package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CepResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioEnderecoResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;

public class EstadioEnderecoResponseMapper {

    public static EstadioEnderecoResponseDTO toEstadioEnderecoResponseDTO(Estadio estadio, CepResponseDTO enderecoViaCep) {
        return new EstadioEnderecoResponseDTO(
                estadio.getNome(),
                enderecoViaCep
        );
    }
}
