package br.com.meli.teamcubation_partidas_de_futebol.partida.service;

import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.PartidaNaoEncontradaException;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaUtil;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class BuscarPartidaServiceTest {
    BuscarPartidaService  buscarPartidaService;
    PartidaRepository partidaRepository;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        partidaRepository = Mockito.mock(PartidaRepository.class);
        buscarPartidaService = new BuscarPartidaService(partidaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            ",,,,,",                        // todos nulos
            "1,,,,,",                       // apenas clubeId
            ",2,,,,",                       // apenas estadioId
            ",,true,,,",                    // apenas goleada
            ",,,true,,",                    // apenas mandante
            ",,,,true,",                    // apenas visitante
            "1,2,true,true,false,"          // todos combinados
    })
    void deveListarPartidasFiltradasComSucesso(
            Long clubeId, Long estadioId, Boolean goleada, Boolean mandante, Boolean visitante){
        Page<Partida> partidasPage = PartidaUtil.criarPagePartidas(2);

        Mockito.when(partidaRepository.findByFiltros(
                Mockito.eq(clubeId),
                Mockito.eq(estadioId),
                Mockito.any(Pageable.class)
        )).thenReturn(partidasPage);

        Page<Partida> partidasPageRetornadas = buscarPartidaService.listarPartidasFiltradas(
                clubeId, estadioId, goleada, mandante, visitante, PageRequest.of(0, 10));

        long esperado = partidasPage.getContent().stream()
                .filter(partida -> goleada == null || partida.isGoleada() == goleada)
                .filter(partida -> mandante == null || (partida.getClubeMandante().getId().equals(clubeId) == mandante))
                .filter(partida -> visitante == null || (partida.getClubeVisitante().getId().equals(clubeId) == visitante))
                .count();

        Assertions.assertEquals(esperado, partidasPageRetornadas.getTotalElements());

        Mockito.verify(partidaRepository, Mockito.times(1)).findByFiltros(
                Mockito.eq(clubeId),
                Mockito.eq(estadioId),
                Mockito.any(Pageable.class)
        );
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumaPartidaAtendeOsFiltros() {
        Page<Partida> partidasMock = PartidaUtil.criarPagePartidas(2);

        Mockito.when(partidaRepository.findByFiltros(
                Mockito.any(), Mockito.any(), Mockito.any(Pageable.class))
        ).thenReturn(partidasMock);

        Long clubeIdFiltro = 999L;
        Boolean goleadaFiltro = true;
        Boolean mandanteFiltro = true;
        Boolean visitanteFiltro = false;

        Page<Partida> resultado = buscarPartidaService.listarPartidasFiltradas(
                clubeIdFiltro, null, goleadaFiltro, mandanteFiltro, visitanteFiltro, PageRequest.of(0, 10));

        Assertions.assertTrue(resultado.isEmpty());
        Assertions.assertEquals(0, resultado.getTotalElements());
        Mockito.verify(partidaRepository, Mockito.times(1)).findByFiltros(
                Mockito.eq(clubeIdFiltro), Mockito.eq(null), Mockito.any(Pageable.class)
        );
    }

    @Test
    void deveBuscarPartidaPorIdComSucesso() {
        Long id = 1L;
        Partida partida = PartidaUtil.criarPartida();
        partida.setId(id);
        Optional<Partida> partidaOptional = Optional.of(partida);

        Mockito.when(partidaRepository.findById(id)).thenReturn(partidaOptional);

        Partida partidaEncontrada = buscarPartidaService.buscarPartidaPorId(id);

        Assertions.assertNotNull(partidaEncontrada);
        Assertions.assertEquals(partida, partidaEncontrada);
        Assertions.assertEquals(id, partidaEncontrada.getId());
        Assertions.assertEquals(partida.getEstadio(), partidaEncontrada.getEstadio());
        Assertions.assertEquals(partida.getClubeMandante(), partidaEncontrada.getClubeMandante());
        Assertions.assertEquals(partida.getClubeVisitante(), partidaEncontrada.getClubeVisitante());
        Assertions.assertEquals(partida.getGolsMandante(), partidaEncontrada.getGolsMandante());
        Assertions.assertEquals(partida.getGolsVisitante(), partidaEncontrada.getGolsVisitante());
        Assertions.assertEquals(partida.getDataHora(), partidaEncontrada.getDataHora());
        Assertions.assertEquals(partida.getDataAtualizacao(), partidaEncontrada.getDataAtualizacao());
        Assertions.assertEquals(partida.getResultado(), partidaEncontrada.getResultado());
        Assertions.assertEquals(partida.isGoleada(), partidaEncontrada.isGoleada());

        Mockito.verify(partidaRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void deveLancarPartidaNaoEncontradaException_quandoPartidaNaoExistir() {
        Long id = 999L;

        Mockito.when(partidaRepository.findById(id)).thenReturn(Optional.empty());

        PartidaNaoEncontradaException exception = Assertions.assertThrows(
                PartidaNaoEncontradaException.class, () -> buscarPartidaService.buscarPartidaPorId(id)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Partida com id " + id + " não encontrada.", exception.getMessage());
        Mockito.verify(partidaRepository, Mockito.times(1)).findById(id);

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

}
