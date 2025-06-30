package br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;

public class ClubeResponseMapper {
    public static ClubeResponseDTO toClubeResponseDTO(Clube clube) {
        ClubeResponseDTO clubeResponseDTO = new ClubeResponseDTO();
        clubeResponseDTO.setNome(clube.getNome());
        clubeResponseDTO.setSiglaEstado(clube.getSiglaEstado());
        clubeResponseDTO.setDataCriacao(clube.getDataCriacao());
        return clubeResponseDTO;
    }
}
