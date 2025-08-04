package br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.AtualizarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;

import java.time.LocalDateTime;

public class AtualizarClubeRequestMapper {
    public static Clube updateEntity(AtualizarClubeRequestDTO clubeAtualizado, Clube clubeCadastrado) {
        clubeCadastrado.setNome(clubeAtualizado.getNome());
        clubeCadastrado.setSiglaEstado(clubeAtualizado.getSiglaEstado().toUpperCase());
        clubeCadastrado.setDataCriacao(clubeAtualizado.getDataCriacao());
        clubeCadastrado.setDataAtualizacao(LocalDateTime.now());
        if(clubeAtualizado.getAtivo() != null){
            clubeCadastrado.setAtivo(clubeAtualizado.getAtivo());
        }
        return clubeCadastrado;
    }
}
