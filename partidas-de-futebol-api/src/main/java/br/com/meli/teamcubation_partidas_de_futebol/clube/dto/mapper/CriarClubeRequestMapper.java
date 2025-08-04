package br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.CriarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;

public class CriarClubeRequestMapper {
    public static Clube toEntity(CriarClubeRequestDTO criarClubeRequestDTO) {
        Clube clube = new Clube();
        clube.setNome(criarClubeRequestDTO.getNome());
        clube.setSiglaEstado(criarClubeRequestDTO.getSiglaEstado().toUpperCase());
        clube.setDataCriacao(criarClubeRequestDTO.getDataCriacao());
        clube.setAtivo(true);
        return clube;
    }
}
