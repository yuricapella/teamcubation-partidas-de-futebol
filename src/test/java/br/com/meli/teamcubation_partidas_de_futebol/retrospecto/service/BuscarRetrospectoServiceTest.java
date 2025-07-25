package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaUtil;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.mapper.RetrospectosAdversariosMapper;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoAdversario;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.util.RetrospectoPrintUtil;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.util.RetrospectoUtil;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    void deveRetornarRetrospectoVazio_quandoClubeNaoTerPartidas(Boolean mandante, Boolean visitante) {
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

    @ParameterizedTest
    @CsvSource({
            ",",
            "true,",
            ",true",
            "true,true"
    })
    void deveLancarClubeNaoEncontradoException_quandoClubeNaoExistir_aoBuscarRetrospecto(Boolean mandante, Boolean visitante) {
        Long id = 1L;

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenThrow(new ClubeNaoEncontradoException(id));

        ClubeNaoEncontradoException exception = Assertions.assertThrows(ClubeNaoEncontradoException.class,
                () -> buscarRetrospectoService.buscarRetrospectoClube(id, mandante, visitante));


        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Clube com id " + id + " não encontrado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, partidaRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "true,",
            ",true",
            "true,true"
    })
    void deveBuscarRetrospectoContraAdversariosComSucesso(Boolean mandante, Boolean visitante) {
        Long clubeId = 1L;
        Long clubeId3 = 3L;
        Partida partidaComClubeId2Visitante = PartidaUtil.criarPartidaComTesteUtils();
        Partida partidaComClubeId2Mandante = PartidaUtil.criarPartidaComTesteUtilsTrocandoVisitanteMandante();
        Partida partidaComClubeId3Visitante = PartidaUtil.criarPartidaComTesteUtilsInformandoIds(clubeId,clubeId3);

        Clube clube = partidaComClubeId2Visitante.getClubeMandante();

        List<Partida> partidas = List.of(partidaComClubeId2Visitante,partidaComClubeId2Mandante,partidaComClubeId3Visitante);

        List<Partida> partidasFiltradas = buscarRetrospectoService.filtrarPartidasPorMandanteVisitante(partidas, clubeId, mandante, visitante);

        Map<Clube, List<Partida>> partidasPorAdversario = partidasFiltradas.stream()
                .collect(Collectors.groupingBy(
                        partida -> partida.getClubeMandante().getId().equals(clubeId)
                                ? partida.getClubeVisitante()
                                : partida.getClubeMandante()
                ));

        List<RetrospectoAdversario> retrospectos = partidasPorAdversario.entrySet().stream()
                .map(entry -> new RetrospectoAdversario(clubeId, entry.getKey(), entry.getValue()))
                .toList();

        RetrospectoAdversariosResponseDTO retrospectosEsperadosDTO =
                RetrospectosAdversariosMapper.toDTO(clube.getNome(),clube.getSiglaEstado(),retrospectos);

        Mockito.when(buscarClubeService.buscarClubePorId(clubeId)).thenReturn(clube);
        Mockito.when(partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(clubeId, clubeId)).thenReturn(partidas);

        RetrospectoAdversariosResponseDTO resultado = buscarRetrospectoService.buscarRetrospectoClubeContraAdversarios(clubeId, mandante, visitante);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(retrospectosEsperadosDTO.getNomeClube(), resultado.getNomeClube());
        Assertions.assertEquals(retrospectosEsperadosDTO.getEstadoClube(), resultado.getEstadoClube());
        Assertions.assertEquals(retrospectosEsperadosDTO.getRetrospectoContraAdversarios().size(),
                resultado.getRetrospectoContraAdversarios().size());
        if (!resultado.getRetrospectoContraAdversarios().isEmpty()){
            Assertions.assertEquals(retrospectosEsperadosDTO.getRetrospectoContraAdversarios().getFirst().getNomeAdversario(),
                    resultado.getRetrospectoContraAdversarios().getFirst().getNomeAdversario());
            Assertions.assertEquals(retrospectosEsperadosDTO.getRetrospectoContraAdversarios().getFirst().getEstadoAdversario(),
                    resultado.getRetrospectoContraAdversarios().getFirst().getEstadoAdversario());
            Assertions.assertEquals(retrospectosEsperadosDTO.getRetrospectoContraAdversarios().getFirst().getJogos(),
                    resultado.getRetrospectoContraAdversarios().getFirst().getJogos());
            Assertions.assertEquals(retrospectosEsperadosDTO.getRetrospectoContraAdversarios().getFirst().getVitorias(),
                    resultado.getRetrospectoContraAdversarios().getFirst().getVitorias());
            Assertions.assertEquals(retrospectosEsperadosDTO.getRetrospectoContraAdversarios().getFirst().getDerrotas(),
                    resultado.getRetrospectoContraAdversarios().getFirst().getDerrotas());
            Assertions.assertEquals(retrospectosEsperadosDTO.getRetrospectoContraAdversarios().getFirst().getEmpates(),
                    resultado.getRetrospectoContraAdversarios().getFirst().getEmpates());
            Assertions.assertEquals(retrospectosEsperadosDTO.getRetrospectoContraAdversarios().getFirst().getGolsFeitos(),
                    resultado.getRetrospectoContraAdversarios().getFirst().getGolsFeitos());
            Assertions.assertEquals(retrospectosEsperadosDTO.getRetrospectoContraAdversarios().getFirst().getGolsSofridos(),
                    resultado.getRetrospectoContraAdversarios().getFirst().getGolsSofridos());
        }
        InOrder inOrder = Mockito.inOrder(buscarClubeService, partidaRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeId);
        inOrder.verify(partidaRepository, Mockito.times(1)).findByClubeMandanteIdOrClubeVisitanteId(clubeId, clubeId);
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
    void deveRetornarRetrospectoContraAdversariosVazio_quandoClubeNaoTerPartidas(Boolean mandante, Boolean visitante) {
        Long clubeId = 1L;
        Clube clube = ClubeUtil.criarClube(clubeId);
        List<Partida> partidas = List.of();

        List<Partida> partidasFiltradas = List.of();

        Map<Clube, List<Partida>> partidasPorAdversario = partidasFiltradas.stream()
                .collect(Collectors.groupingBy(
                        partida -> partida.getClubeMandante().getId().equals(clubeId)
                                ? partida.getClubeVisitante()
                                : partida.getClubeMandante()
                ));

        List<RetrospectoAdversario> retrospectos = partidasPorAdversario.entrySet().stream()
                .map(entry -> new RetrospectoAdversario(clubeId, entry.getKey(), entry.getValue()))
                .toList();

        RetrospectoAdversariosResponseDTO retrospectosEsperadosDTO =
                RetrospectosAdversariosMapper.toDTO(clube.getNome(),clube.getSiglaEstado(),retrospectos);

        Mockito.when(buscarClubeService.buscarClubePorId(clubeId)).thenReturn(clube);
        Mockito.when(partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(clubeId, clubeId)).thenReturn(partidas);

        RetrospectoAdversariosResponseDTO resultado = buscarRetrospectoService.buscarRetrospectoClubeContraAdversarios(clubeId, mandante, visitante);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(retrospectosEsperadosDTO.getNomeClube(), resultado.getNomeClube());
        Assertions.assertEquals(retrospectosEsperadosDTO.getEstadoClube(), resultado.getEstadoClube());
        Assertions.assertEquals(retrospectosEsperadosDTO.getRetrospectoContraAdversarios().size(),
                resultado.getRetrospectoContraAdversarios().size());
        Assertions.assertTrue(resultado.getRetrospectoContraAdversarios().isEmpty());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, partidaRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeId);
        inOrder.verify(partidaRepository, Mockito.times(1)).findByClubeMandanteIdOrClubeVisitanteId(clubeId, clubeId);
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
    void deveLancarClubeNaoEncontradoException_quandoClubeNaoExistir_aoBuscarRetrospectoAdversario(Boolean mandante, Boolean visitante) {
        Long clubeId = 1L;

        Mockito.when(buscarClubeService.buscarClubePorId(clubeId)).thenThrow(new ClubeNaoEncontradoException(clubeId));

        ClubeNaoEncontradoException exception = Assertions.assertThrows(ClubeNaoEncontradoException.class,
                () -> buscarRetrospectoService.buscarRetrospectoClubeContraAdversarios(clubeId, mandante, visitante));


        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Clube com id " + clubeId + " não encontrado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, partidaRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeId);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }
}
