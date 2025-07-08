package br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.AtualizarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;

import java.time.LocalDateTime;

public class AtualizarPartidaRequestMapper {
    public static Partida updateEntity(AtualizarPartidaRequestDTO atualizarPartidaRequestDTO, Partida partidaComDadosAntigos, Clube clubeMandante, Clube clubeVisitante, Estadio estadio) {
        partidaComDadosAntigos.setClubeMandante(clubeMandante);
        partidaComDadosAntigos.setClubeVisitante(clubeVisitante);
        partidaComDadosAntigos.setEstadio(estadio);
        partidaComDadosAntigos.setGolsMandante(atualizarPartidaRequestDTO.getGolsMandante());
        partidaComDadosAntigos.setGolsVisitante(atualizarPartidaRequestDTO.getGolsVisitante());
        partidaComDadosAntigos.setDataHora(atualizarPartidaRequestDTO.getDataHora());
        partidaComDadosAntigos.setDataAtualizacao(LocalDateTime.now());
        return partidaComDadosAntigos;
    }
}
