package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.util;

import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;

public class RetrospectoPrintUtil {
    public static void printResumo(Retrospecto retrospecto) {
        System.out.println(
                "Clube: " + retrospecto.getClube().getNome() +
                        " | Jogos: " + retrospecto.getJogos() +
                        " | Vitórias: " + retrospecto.getVitorias() +
                        " | Derrotas: " + retrospecto.getDerrotas() +
                        " | Empates: " + retrospecto.getEmpates() +
                        " | Gols Feitos: " + retrospecto.getGolsFeitos() +
                        " | Gols Sofridos: " + retrospecto.getGolsSofridos()
        );
    }
}
