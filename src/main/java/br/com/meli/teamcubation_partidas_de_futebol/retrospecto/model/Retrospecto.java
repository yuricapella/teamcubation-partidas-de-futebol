package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.ClubeResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;

import java.util.List;

public class Retrospecto {
    private final ClubeResponseDTO clube;
    private final int jogos;
    private final int vitorias;
    private final int derrotas;
    private final int empates;
    private final int golsFeitos;
    private final int golsSofridos;

    public Retrospecto(Clube clube, List<Partida> partidas) {
        this.clube = ClubeResponseMapper.toClubeResponseDTO(clube);
        this.jogos = partidas.size();
        int vitorias = 0;
        int derrotas = 0;
        int empates = 0;
        int golsFeitos = 0;
        int golsSofridos = 0;

        for (Partida partida : partidas) {
            if (partida.getClubeMandante().getId().equals(clube.getId())) {
                golsFeitos += partida.getGolsMandante();
                golsSofridos += partida.getGolsVisitante();
                switch (partida.getResultado()) {
                    case VITORIA_MANDANTE -> vitorias++;
                    case VITORIA_VISITANTE -> derrotas++;
                    case EMPATE -> empates++;
                }
            } else if (partida.getClubeVisitante().getId().equals(clube.getId())) {
                golsFeitos += partida.getGolsVisitante();
                golsSofridos += partida.getGolsMandante();
                switch (partida.getResultado()) {
                    case VITORIA_MANDANTE -> derrotas++;
                    case VITORIA_VISITANTE -> vitorias++;
                    case EMPATE -> empates++;
                }
            }
        }

        this.vitorias = vitorias;
        this.derrotas = derrotas;
        this.empates = empates;
        this.golsFeitos = golsFeitos;
        this.golsSofridos = golsSofridos;
    }

    public ClubeResponseDTO getClube() {
        return clube;
    }

    public int getJogos() {
        return jogos;
    }

    public int getVitorias() { return vitorias; }
    public int getDerrotas() { return derrotas; }
    public int getEmpates() { return empates; }
    public int getGolsFeitos() { return golsFeitos; }
    public int getGolsSofridos() { return golsSofridos; }

}
