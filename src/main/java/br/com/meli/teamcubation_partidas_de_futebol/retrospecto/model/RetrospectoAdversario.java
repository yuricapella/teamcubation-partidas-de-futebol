package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;

import java.util.List;

public class RetrospectoAdversario {
    private final String nomeAdversario;
    private final String estadoAdversario;
    private final int jogos;
    private final int vitorias;
    private final int derrotas;
    private final int empates;
    private final int golsFeitos;
    private final int golsSofridos;

    public RetrospectoAdversario(Long idClube, Clube clubeAdversario, List<Partida> partidas) {
        this.nomeAdversario = clubeAdversario.getNome();
        this.estadoAdversario = clubeAdversario.getSiglaEstado();
        this.jogos = partidas.size();
        int vitorias = 0;
        int derrotas = 0;
        int empates = 0;
        int golsFeitos = 0;
        int golsSofridos = 0;

        for (Partida partida : partidas) {
            if (partida.getClubeMandante().getId().equals(idClube)) {
                golsFeitos += partida.getGolsMandante();
                golsSofridos += partida.getGolsVisitante();
                switch (partida.getResultado()) {
                    case VITORIA_MANDANTE -> vitorias++;
                    case VITORIA_VISITANTE -> derrotas++;
                    case EMPATE -> empates++;
                }
            } else if (partida.getClubeVisitante().getId().equals(idClube)) {
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

    public String getNomeAdversario() {
        return nomeAdversario;
    }

    public String getEstadoAdversario() {
        return estadoAdversario;
    }

    public int getJogos() {
        return jogos;
    }

    public int getVitorias() {
        return vitorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getEmpates() {
        return empates;
    }

    public int getGolsFeitos() {
        return golsFeitos;
    }

    public int getGolsSofridos() {
        return golsSofridos;
    }
}
