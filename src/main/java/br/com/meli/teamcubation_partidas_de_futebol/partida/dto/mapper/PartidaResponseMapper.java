package br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.ClubeResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper.EstadioResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;

public class PartidaResponseMapper {
    public static PartidaResponseDTO toPartidaResponseDTO(Partida partida) {
        PartidaResponseDTO partidaDTO = new PartidaResponseDTO();
        partidaDTO.setClubeMandante(ClubeResponseMapper.toClubeResponseDTO(partida.getClubeMandante()));
        partidaDTO.setClubeVisitante(ClubeResponseMapper.toClubeResponseDTO(partida.getClubeVisitante()));
        partidaDTO.setEstadio(EstadioResponseMapper.toEstadioResponseDTO(partida.getEstadio()));
        partidaDTO.setGolsMandante(partida.getGolsMandante());
        partidaDTO.setGolsVisitante(partida.getGolsVisitante());
        partidaDTO.setDataHora(partida.getDataHora());
        partidaDTO.setResultado(partida.getResultado());
        return partidaDTO;
    }
}
