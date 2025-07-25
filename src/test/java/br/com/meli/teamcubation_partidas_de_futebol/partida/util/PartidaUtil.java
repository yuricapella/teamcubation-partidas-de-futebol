package br.com.meli.teamcubation_partidas_de_futebol.partida.util;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.util.EstadioUtil;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartidaUtil {

    public static Partida criarPartida() {
        Clube clubeMandante = new Clube();
        clubeMandante.setId(1L);
        clubeMandante.setNome("Mandante");
        clubeMandante.setSiglaEstado("SP");
        clubeMandante.setDataCriacao(LocalDate.of(2020, 1, 1));

        Clube clubeVisitante = new Clube();
        clubeVisitante.setId(2L);
        clubeVisitante.setNome("Visitante");
        clubeVisitante.setSiglaEstado("RJ");
        clubeVisitante.setDataCriacao(LocalDate.of(2021, 1, 1));

        Estadio estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Estádio Teste");

        LocalDateTime dataHora = LocalDateTime.of(2023, 7, 10, 16, 0, 0);

        return new Partida(estadio, clubeMandante, clubeVisitante, 2, 1, dataHora);
    }

    public static Page<Partida> criarPagePartidas(int quantidade) {
        List<Partida> partidas = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            Partida partida = criarPartida();
            partida.setId((long) i);

            partida.setGolsMandante(2 + i);
            partida.setGolsVisitante(1 + (i % 3));
            partida.setDataHora(partida.getDataHora().plusDays(i - 1));

            partidas.add(partida);
        }

        return new PageImpl<>(partidas, org.springframework.data.domain.PageRequest.of(0, 10), quantidade);
    }

    public static Partida criarPartidaComTesteUtils() {
        Clube clubeMandante = ClubeUtil.criarClube(1L);
        Clube clubeVisitante = ClubeUtil.criarClube(2L);
        Estadio estadio = EstadioUtil.criarEstadio(1L);
        LocalDateTime dataHora = LocalDateTime.of(2023, 7, 10, 16, 0, 0);
        return new Partida(estadio, clubeMandante, clubeVisitante, 0, 0, dataHora);
    }

    public static Partida criarPartidaComTesteUtilsTrocandoVisitanteMandante() {
        Clube clubeMandante = ClubeUtil.criarClube(2L);
        Clube clubeVisitante = ClubeUtil.criarClube(1L);
        Estadio estadio = EstadioUtil.criarEstadio(1L);
        LocalDateTime dataHora = LocalDateTime.of(2023, 7, 10, 16, 0, 0);
        return new Partida(estadio, clubeMandante, clubeVisitante, 0, 0, dataHora);
    }

    public static List<Partida> criarListPartidasComTesteUtils(int quantidade) {
        List<Partida> partidas = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            Partida partida = criarPartidaComTesteUtils();
            partida.setId((long) i);

            partida.setGolsMandante(2 + i);
            partida.setGolsVisitante(1 + (i % 3));
            partida.setDataHora(partida.getDataHora().plusDays(i - 1));

            partidas.add(partida);
        }
        return partidas;
    }
}
