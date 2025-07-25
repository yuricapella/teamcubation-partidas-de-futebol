package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaUtil;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.util.RetrospectoPrintUtil;
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

import java.util.List;

public class BuscarRetrospectoServiceTest {
    BuscarRetrospectoService buscarRetrospectoService;
    BuscarClubeService buscarClubeService;
    PartidaRepository partidaRepository;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        buscarClubeService = Mockito.mock(BuscarClubeService.class);
        partidaRepository = Mockito.mock(PartidaRepository.class);
        buscarRetrospectoService = new BuscarRetrospectoService(buscarClubeService, partidaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "true,",
            ",true",
            "true,true"
    })
    void deveBuscarRetrospectoDeUmClubeComSucesso(Boolean mandante, Boolean visitante) {
        Long id = 1L;
        List<Partida> partidas = PartidaUtil.criarListPartidasComTesteUtils(2);
        Partida partidaIdInvertido = PartidaUtil.criarPartidaComTesteUtilsTrocandoVisitanteMandante();
        partidas.add(partidaIdInvertido);
        Clube clube = partidas.getFirst().getClubeMandante();

        List<Partida> partidasFiltradas = buscarRetrospectoService.filtrarPartidasPorMandanteVisitante(partidas, id, mandante, visitante);

        Retrospecto retrospectoEsperado = RetrospectoUtil.criaRetrospecto(clube, partidasFiltradas);

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenReturn(clube);
        Mockito.when(partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(id, id)).thenReturn(partidas);

        Retrospecto resultado = buscarRetrospectoService.buscarRetrospectoClube(id, mandante, visitante);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(retrospectoEsperado.getClube().getNome(), resultado.getClube().getNome());
        Assertions.assertEquals(retrospectoEsperado.getJogos(), resultado.getJogos());
        Assertions.assertEquals(retrospectoEsperado.getVitorias(), resultado.getVitorias());
        Assertions.assertEquals(retrospectoEsperado.getDerrotas(), resultado.getDerrotas());
        Assertions.assertEquals(retrospectoEsperado.getEmpates(), resultado.getEmpates());
        Assertions.assertEquals(retrospectoEsperado.getGolsFeitos(), resultado.getGolsFeitos());
        Assertions.assertEquals(retrospectoEsperado.getGolsSofridos(), resultado.getGolsSofridos());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, partidaRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
        inOrder.verify(partidaRepository, Mockito.times(1)).findByClubeMandanteIdOrClubeVisitanteId(id, id);
        inOrder.verifyNoMoreInteractions();

        RetrospectoPrintUtil.printResumo(resultado);
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "true,",
            ",true",
            "true,true"
    })
    void deveRetornarListaVazia_quandoClubeNaoTerPartidas(Boolean mandante, Boolean visitante) {
        Long id = 1L;
        List<Partida> partidas = List.of();

        Clube clube = ClubeUtil.criarClube(id);

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenReturn(clube);
        Mockito.when(partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(id, id)).thenReturn(partidas);

        Retrospecto resultado = buscarRetrospectoService.buscarRetrospectoClube(id, mandante, visitante);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(clube.getNome(), resultado.getClube().getNome());
        Assertions.assertEquals(0, resultado.getJogos());
        Assertions.assertEquals(0, resultado.getVitorias());
        Assertions.assertEquals(0, resultado.getDerrotas());
        Assertions.assertEquals(0, resultado.getEmpates());
        Assertions.assertEquals(0, resultado.getGolsFeitos());
        Assertions.assertEquals(0, resultado.getGolsSofridos());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, partidaRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
        inOrder.verify(partidaRepository, Mockito.times(1)).findByClubeMandanteIdOrClubeVisitanteId(id, id);
        inOrder.verifyNoMoreInteractions();

        RetrospectoPrintUtil.printResumo(resultado);
    }

    @Test
    void deveRetornarClubeNaoEncontradoException_quandoClubeNaoExistir() {
        Long id = 1L;

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenThrow(new ClubeNaoEncontradoException(id));

        ClubeNaoEncontradoException exception = Assertions.assertThrows(ClubeNaoEncontradoException.class,
                () -> buscarClubeService.buscarClubePorId(id));


        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Clube com id " + id + " não encontrado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, partidaRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }


}
