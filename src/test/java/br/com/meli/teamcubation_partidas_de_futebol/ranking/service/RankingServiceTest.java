package br.com.meli.teamcubation_partidas_de_futebol.ranking.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaUtil;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.model.Ranking;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.strategy.CalculadoraRankingStrategy;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.util.RankingUtil;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.util.TipoRanking;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.util.RetrospectoUtil;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingServiceTest {
    RankingService rankingService;
    List<CalculadoraRankingStrategy> strategies;
    PartidaRepository partidaRepository;
    ClubeRepository clubeRepository;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        strategies = new ArrayList<>();
        partidaRepository = Mockito.mock(PartidaRepository.class);
        clubeRepository = Mockito.mock(ClubeRepository.class);
        rankingService = new RankingService(strategies, partidaRepository, clubeRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "TOTAL_PONTOS",
            "TOTAL_VITORIAS",
            "TOTAL_GOLS",
            "TOTAL_JOGOS"
    })
    void deveBuscarRanking_comSucesso(String tipoRankingstring) {
        TipoRanking tipoRanking = TipoRanking.fromString(tipoRankingstring);

        Long id1 = 1L;
        Long id2 = 2L;
        Clube clube1 = ClubeUtil.criarClube(id1);
        Clube clube2 = ClubeUtil.criarClube(id2);
        Retrospecto retrospecto1 = RetrospectoUtil.criaRetrospecto(clube1);
        Retrospecto retrospecto2 = RetrospectoUtil.criaRetrospecto(clube2);
        List<Clube> clubes = List.of(clube1, clube2);
        List<Partida> partidaList = PartidaUtil.criarListPartidasComTesteUtils(2);

        Map<Long, Retrospecto> retrospectosPorClube = new HashMap<>();
        retrospectosPorClube.put(id1, retrospecto1);
        retrospectosPorClube.put(id2, retrospecto2);

        CalculadoraRankingStrategy strategy = RankingUtil.criarCalculadoraRanking(tipoRanking);

        strategies.add(strategy);

        List<? extends Ranking> listaEsperada = strategy.calcular(clubes, retrospectosPorClube);

        Mockito.when(clubeRepository.findAll()).thenReturn(clubes);
        Mockito.when(partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(partidaList);

        List<? extends Ranking> resultado = rankingService.calcularRanking(tipoRankingstring);

        Assertions.assertEquals(listaEsperada.size(), resultado.size());
        for (int i = 0; i < listaEsperada.size(); i++) {
            var esperado = listaEsperada.get(i);
            var real = resultado.get(i);
            Assertions.assertEquals(esperado.getNomeClube(), real.getNomeClube());
            Assertions.assertEquals(esperado.getEstadoClube(), real.getEstadoClube());
            Assertions.assertEquals(esperado.getTotal(), real.getTotal());
            Assertions.assertTrue(real.getTotal() > 0);
        }
        for (int i = 1; i < resultado.size(); i++) {
            Assertions.assertTrue(resultado.get(i-1).getTotal() >= resultado.get(i).getTotal());
        }

        InOrder inOrder = Mockito.inOrder(clubeRepository, partidaRepository);
        inOrder.verify(clubeRepository, Mockito.times(1)).findAll();
        inOrder.verify(partidaRepository, Mockito.times(2)).findByClubeMandanteIdOrClubeVisitanteId(Mockito.anyLong(), Mockito.anyLong());
    }

    @ParameterizedTest
    @CsvSource({
            "TOTAL_PONTOS",
            "TOTAL_VITORIAS",
            "TOTAL_GOLS",
            "TOTAL_JOGOS"
    })
    void deveRetornarListaVazia_quandoNaoExistiremClubes(String tipoRankingstring) {
        TipoRanking tipoRanking = TipoRanking.fromString(tipoRankingstring);

        Map<Long, Retrospecto> retrospectosPorClube = new HashMap<>();

        CalculadoraRankingStrategy strategy = RankingUtil.criarCalculadoraRanking(tipoRanking);

        strategies.add(strategy);

        List<? extends Ranking> listaEsperada = strategy.calcular(List.of(), retrospectosPorClube);

        Mockito.when(clubeRepository.findAll()).thenReturn(List.of());
        Mockito.when(partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(List.of());

        List<? extends Ranking> resultado = rankingService.calcularRanking(tipoRankingstring);

        Assertions.assertEquals(listaEsperada.size(), resultado.size());
        Assertions.assertTrue(resultado.isEmpty());

        InOrder inOrder = Mockito.inOrder(clubeRepository, partidaRepository);
        inOrder.verify(clubeRepository, Mockito.times(1)).findAll();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void deveLancarExcecao_quandoTipoRankingInvalido() {
        String tipoInvalido = "TIPO_NAO_EXISTENTE";

        ResponseStatusException ex = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> rankingService.calcularRanking(tipoInvalido)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        Assertions.assertEquals("tipoRanking inválido! Use valores: TOTAL_PONTOS, TOTAL_GOLS, TOTAL_VITORIAS, TOTAL_JOGOS.", ex.getReason());

        PrintUtil.printMensagemDeErro(ex.getMessage());

        InOrder inOrder = Mockito.inOrder(clubeRepository, partidaRepository);
        inOrder.verifyNoMoreInteractions();
    }

}
