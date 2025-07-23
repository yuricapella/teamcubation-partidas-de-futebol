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
import org.mockito.InOrder;
import org.mockito.Mockito;

public class DeletarPartidaServiceTest {
    DeletarPartidaService deletarPartidaService;
    PartidaRepository partidaRepository;
    BuscarPartidaService buscarPartidaService;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        partidaRepository = Mockito.mock(PartidaRepository.class);
        buscarPartidaService = Mockito.mock(BuscarPartidaService.class);
        deletarPartidaService = new DeletarPartidaService(partidaRepository, buscarPartidaService);
    }


    @Test
    void deveDeletarPartidaPorIdComSucesso() {
        Long partidaId = 1L;
        Partida partida = PartidaUtil.criarPartida();
        partida.setId(partidaId);

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId)).thenReturn(partida);

        deletarPartidaService.deletarPartidaPorId(partidaId);

        InOrder inOrder = Mockito.inOrder(buscarPartidaService, partidaRepository);
        inOrder.verify(buscarPartidaService).buscarPartidaPorId(partidaId);
        inOrder.verify(partidaRepository).delete(partida);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void deveLancarPartidaNaoEncontradaException_quandoPartidaNaoExistir() {
        Long partidaId = 999L;

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId))
                .thenThrow(new PartidaNaoEncontradaException(partidaId));

        PartidaNaoEncontradaException exception = Assertions.assertThrows(
                PartidaNaoEncontradaException.class,
                () -> deletarPartidaService.deletarPartidaPorId(partidaId)
        );

        Assertions.assertEquals("Partida com id " + partidaId + " não encontrada.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarPartidaService);
        inOrder.verify(buscarPartidaService).buscarPartidaPorId(partidaId);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }
}
