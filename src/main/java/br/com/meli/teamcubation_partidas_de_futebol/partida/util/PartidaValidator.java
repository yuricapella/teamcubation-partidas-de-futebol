package br.com.meli.teamcubation_partidas_de_futebol.partida.util;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.ClubeInativoException;
import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.ClubesIguaisException;
import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.DataPartidaAnteriorACriacaoDoClubeException;

import java.time.LocalDate;

public class PartidaValidator {

    public static void validarCriacaoDePartidas(Clube clubeMandante, Clube clubeVisitante, Estadio estadio, CriarPartidaRequestDTO partidaACriar) {
        validarClubesIguais(clubeMandante,clubeVisitante);
        validarDataPartidaAnteriorACriacaoClube(partidaACriar,clubeMandante,clubeVisitante);
        validarClubeInativo(clubeMandante,clubeVisitante);
    }

    public static void validarClubesIguais(Clube clubeMandante, Clube clubeVisitante) {
        if(clubeMandante.getId().equals(clubeVisitante.getId())){
            throw new ClubesIguaisException();
        }
    }

    public static void validarDataPartidaAnteriorACriacaoClube(CriarPartidaRequestDTO partidaACriar, Clube clubeMandante, Clube clubeVisitante) {
        LocalDate dataPartida = partidaACriar.getDataHora().toLocalDate();
        if(dataPartida.isBefore(clubeMandante.getDataCriacao()) || dataPartida.isBefore(clubeVisitante.getDataCriacao())){
            throw new DataPartidaAnteriorACriacaoDoClubeException();
        }
    }

    public static void validarClubeInativo(Clube clubeMandante, Clube clubeVisitante) {
        if(clubeMandante.getAtivo() == false || clubeVisitante.getAtivo() == false){
            throw new ClubeInativoException();
        }
    }

}
