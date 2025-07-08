package br.com.meli.teamcubation_partidas_de_futebol.partida.util;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.AtualizarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.*;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PartidaValidator {
    private final PartidaRepository partidaRepository;

    public PartidaValidator(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    public void validarCriacaoDePartidas(Clube clubeMandante, Clube clubeVisitante, Estadio estadio, CriarPartidaRequestDTO partidaACriar) {
        validarClubesIguais(clubeMandante,clubeVisitante);
        validarDataPartidaAnteriorACriacaoClubeCriacao(partidaACriar,clubeMandante,clubeVisitante);
        validarClubeInativo(clubeMandante,clubeVisitante);
        validarPartidasDosClubesComHorariosProximos(clubeMandante.getId(),clubeVisitante.getId(),partidaACriar.getDataHora());
        validarEstadioJaPossuiPartidasNoMesmoDiaCriacao(partidaACriar);
    }

    public void validarClubesIguais(Clube clubeMandante, Clube clubeVisitante) {
        if(clubeMandante.getId().equals(clubeVisitante.getId())){
            throw new ClubesIguaisException();
        }
    }

    public void validarDataPartidaAnteriorACriacaoClubeCriacao(CriarPartidaRequestDTO partidaACriar, Clube clubeMandante, Clube clubeVisitante) {
        LocalDate dataPartida = partidaACriar.getDataHora().toLocalDate();
        if(dataPartida.isBefore(clubeMandante.getDataCriacao()) || dataPartida.isBefore(clubeVisitante.getDataCriacao())){
            throw new DataPartidaAnteriorACriacaoDoClubeException();
        }
    }

    public void validarClubeInativo(Clube clubeMandante, Clube clubeVisitante) {
        if(clubeMandante.getAtivo() == false || clubeVisitante.getAtivo() == false){
            throw new ClubeInativoException();
        }
    }

    public void validarPartidasDosClubesComHorariosProximos(Long clubeMandanteId, Long clubeVisitanteId, LocalDateTime dataNovaPartida) {
        if(partidaRepository.existeConflitoDeHorario(clubeMandanteId,clubeVisitanteId, dataNovaPartida) > 0){
            throw new ClubesComPartidasEmHorarioMenorQue48HorasException();
        }
    }

    public void validarEstadioJaPossuiPartidasNoMesmoDiaCriacao(CriarPartidaRequestDTO partidaACriar) {
        LocalDate dataPartida = partidaACriar.getDataHora().toLocalDate();
        if(partidaRepository.existePartidaNoEstadioNoMesmoDia(partidaACriar.getEstadioId(),dataPartida) > 0){
            throw new EstadioJaPossuiPartidaNoMesmoDiaException();
        }
    }

    // --- ATUALIZAÇÃO ---
    public void validarAtualizacaoDePartidas(Clube clubeMandante, Clube clubeVisitante, Estadio estadio, AtualizarPartidaRequestDTO dadosParaAtualizar) {
        validarClubesIguais(clubeMandante, clubeVisitante);
        validarDataPartidaAnteriorACriacaoClubeAtualizacao(dadosParaAtualizar, clubeMandante, clubeVisitante);
        validarClubeInativo(clubeMandante, clubeVisitante);
        validarPartidasDosClubesComHorariosProximos(clubeMandante.getId(), clubeVisitante.getId(), dadosParaAtualizar.getDataHora());
        validarEstadioJaPossuiPartidasNoMesmoDiaAtualizacao(dadosParaAtualizar);
    }
    public void validarDataPartidaAnteriorACriacaoClubeAtualizacao(AtualizarPartidaRequestDTO dadosParaAtualizar, Clube clubeMandante, Clube clubeVisitante) {
        LocalDate dataPartida = dadosParaAtualizar.getDataHora().toLocalDate();
        if (dataPartida.isBefore(clubeMandante.getDataCriacao()) || dataPartida.isBefore(clubeVisitante.getDataCriacao())) {
            throw new DataPartidaAnteriorACriacaoDoClubeException();
        }
    }
    public void validarEstadioJaPossuiPartidasNoMesmoDiaAtualizacao(AtualizarPartidaRequestDTO dadosParaAtualizar) {
        LocalDate dataPartida = dadosParaAtualizar.getDataHora().toLocalDate();
        if (partidaRepository.existePartidaNoEstadioNoMesmoDia(dadosParaAtualizar.getEstadioId(), dataPartida) > 0) {
            throw new EstadioJaPossuiPartidaNoMesmoDiaException();
        }
    }
}
