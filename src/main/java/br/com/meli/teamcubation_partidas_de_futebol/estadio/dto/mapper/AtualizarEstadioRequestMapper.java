package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.AtualizarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;

import java.time.LocalDateTime;

public class AtualizarEstadioRequestMapper {
    public static Estadio updateEntity(AtualizarEstadioRequestDTO dadosParaAtualizar, Estadio dadosAntigos) {
        dadosAntigos.setNome(dadosParaAtualizar.getNome());
        dadosAntigos.setDataAtualizacao(LocalDateTime.now());
        return dadosAntigos;
    }
}
