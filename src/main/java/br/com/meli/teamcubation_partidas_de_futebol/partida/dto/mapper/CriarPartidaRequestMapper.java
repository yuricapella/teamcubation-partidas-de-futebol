package br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;

public class CriarPartidaRequestMapper {
    public static Partida toEntity(CriarPartidaRequestDTO criarPartidaRequestDTO, Clube clubeMandante, Clube clubeVisitante, Estadio estadio) {
        Partida partida = new Partida();
        partida.setClubeMandante(clubeMandante);
        partida.setClubeVisitante(clubeVisitante);
        partida.setEstadio(estadio);
        partida.setGolsMandante(criarPartidaRequestDTO.getGolsMandante());
        partida.setGolsVisitante(criarPartidaRequestDTO.getGolsVisitante());
        partida.setDataHora(criarPartidaRequestDTO.getDataHora());
        return partida;
    }
}
