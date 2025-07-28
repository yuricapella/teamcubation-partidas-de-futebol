package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.util;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaUtil;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RetrospectoUtil {

    public static void assertRetrospectoClube(
            ResultActions result,
            int index,
            String nome,
            String sigla,
            int ano,
            int mes,
            int dia,
            int jogos,
            int vitorias,
            int derrotas,
            int empates,
            int golsFeitos,
            int golsSofridos) throws Exception {
        result
                .andExpect(jsonPath("$.retrospectos[" + index + "].clube.nome").value(nome))
                .andExpect(jsonPath("$.retrospectos[" + index + "].clube.siglaEstado").value(sigla))
                .andExpect(jsonPath("$.retrospectos[" + index + "].clube.dataCriacao[0]").value(ano))
                .andExpect(jsonPath("$.retrospectos[" + index + "].clube.dataCriacao[1]").value(mes))
                .andExpect(jsonPath("$.retrospectos[" + index + "].clube.dataCriacao[2]").value(dia))
                .andExpect(jsonPath("$.retrospectos[" + index + "].jogos").value(jogos))
                .andExpect(jsonPath("$.retrospectos[" + index + "].vitorias").value(vitorias))
                .andExpect(jsonPath("$.retrospectos[" + index + "].derrotas").value(derrotas))
                .andExpect(jsonPath("$.retrospectos[" + index + "].empates").value(empates))
                .andExpect(jsonPath("$.retrospectos[" + index + "].golsFeitos").value(golsFeitos))
                .andExpect(jsonPath("$.retrospectos[" + index + "].golsSofridos").value(golsSofridos));
    }

    public static void assertListaPartidas(
            ResultActions result,
            int index,
            String nomeMandante,
            String siglaMandante,
            int anoMandante,
            int mesMandante,
            int diaMandante,
            String nomeVisitante,
            String siglaVisitante,
            int anoVisitante,
            int mesVisitante,
            int diaVisitante,
            String nomeEstadio,
            int golsMandante,
            int golsVisitante,
            String dataHora,
            String resultado
    ) throws Exception {
        result
                .andExpect(jsonPath("$.partidas[" + index + "].clubeMandante.nome").value(nomeMandante))
                .andExpect(jsonPath("$.partidas[" + index + "].clubeMandante.siglaEstado").value(siglaMandante))
                .andExpect(jsonPath("$.partidas[" + index + "].clubeMandante.dataCriacao[0]").value(anoMandante))
                .andExpect(jsonPath("$.partidas[" + index + "].clubeMandante.dataCriacao[1]").value(mesMandante))
                .andExpect(jsonPath("$.partidas[" + index + "].clubeMandante.dataCriacao[2]").value(diaMandante))
                .andExpect(jsonPath("$.partidas[" + index + "].clubeVisitante.nome").value(nomeVisitante))
                .andExpect(jsonPath("$.partidas[" + index + "].clubeVisitante.siglaEstado").value(siglaVisitante))
                .andExpect(jsonPath("$.partidas[" + index + "].clubeVisitante.dataCriacao[0]").value(anoVisitante))
                .andExpect(jsonPath("$.partidas[" + index + "].clubeVisitante.dataCriacao[1]").value(mesVisitante))
                .andExpect(jsonPath("$.partidas[" + index + "].clubeVisitante.dataCriacao[2]").value(diaVisitante))
                .andExpect(jsonPath("$.partidas[" + index + "].estadio.nome").value(nomeEstadio))
                .andExpect(jsonPath("$.partidas[" + index + "].golsMandante").value(golsMandante))
                .andExpect(jsonPath("$.partidas[" + index + "].golsVisitante").value(golsVisitante))
                .andExpect(jsonPath("$.partidas[" + index + "].dataHora").value(dataHora))
                .andExpect(jsonPath("$.partidas[" + index + "].resultado").value(resultado));
    }

    public static Retrospecto criaRetrospecto(Clube clube) {
        List<Partida> partidas = PartidaUtil.criarListPartidasComTesteUtils(2);
        return new Retrospecto(clube,partidas);
    }

    public static Retrospecto criaRetrospecto(Clube clube, List<Partida> partidas) {
        return new Retrospecto(clube,partidas);
    }
}
