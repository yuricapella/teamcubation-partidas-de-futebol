package br.com.meli.teamcubation_partidas_de_futebol.partida.util;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PartidaUtil {

    public static Partida getPartida() {
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

        LocalDateTime dataHora1 = LocalDateTime.of(2023, 7, 10, 16, 0, 0);

        return new Partida(estadio, clubeMandante, clubeVisitante, 2, 1, dataHora1);
    }

    public static Page<Partida> getPagePartida() {
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

        LocalDateTime dataHora1 = LocalDateTime.of(2023, 7, 10, 16, 0, 0);
        LocalDateTime dataHora2 = LocalDateTime.of(2023, 7, 9, 18, 0, 0);

        Partida partida1 = new Partida(estadio, clubeMandante, clubeVisitante, 2, 1, dataHora1);
        Partida partida2 = new Partida(estadio, clubeMandante, clubeVisitante, 3, 3, dataHora2);

        return new PageImpl<>(List.of(partida1, partida2), org.springframework.data.domain.PageRequest.of(0, 10), 2);
    }
}
