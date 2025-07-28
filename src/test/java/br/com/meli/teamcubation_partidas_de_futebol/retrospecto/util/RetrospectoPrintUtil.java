package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.util;

import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoAdversariosResponseDTO;
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

    public static void printResumo(RetrospectoAdversariosResponseDTO response) {
        System.out.println(
                "Clube: " + response.getNomeClube() +
                        " | Estado: " + response.getEstadoClube()
        );
        response.getRetrospectoContraAdversarios().forEach(adversario ->
                System.out.println(
                        "Adversário: " + adversario.getNomeAdversario() +
                                " | Estado: " + adversario.getEstadoAdversario() +
                                " | Jogos: " + adversario.getJogos() +
                                " | Vitórias: " + adversario.getVitorias() +
                                " | Derrotas: " + adversario.getDerrotas() +
                                " | Empates: " + adversario.getEmpates() +
                                " | Gols Feitos: " + adversario.getGolsFeitos() +
                                " | Gols Sofridos: " + adversario.getGolsSofridos()
                )
        );
    }
}
