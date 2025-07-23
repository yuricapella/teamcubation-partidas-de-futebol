package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class InativarClubeServiceTest {
    InativarClubeService inativarClubeService;
    BuscarClubeService buscarClubeService;
    ClubeRepository clubeRepository;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        buscarClubeService = Mockito.mock(BuscarClubeService.class);
        clubeRepository = Mockito.mock(ClubeRepository.class);
        inativarClubeService = new InativarClubeService(buscarClubeService,clubeRepository);
    }

    @Test
    void deveInativarClubePorIdComSucesso() {
        Long id = 1L;
        Clube clube = ClubeUtil.criarClube(id);

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenReturn(clube);
        Mockito.when(clubeRepository.save(Mockito.any(Clube.class))).thenReturn(clube);

        inativarClubeService.inativarClubePorId(id);

        Assertions.assertFalse(clube.getAtivo());
        Assertions.assertNotNull(clube.getDataAtualizacao());

        InOrder inOrder = Mockito.inOrder(buscarClubeService,clubeRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
        inOrder.verify(clubeRepository, Mockito.times(1)).save(clube);
    }

    @Test
    void deveLancarClubeNaoEncontradoException_quandoClubeNaoExistir(){
        Long id = 1L;

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenThrow( new ClubeNaoEncontradoException(id));

        ClubeNaoEncontradoException exception = Assertions.assertThrows(
                ClubeNaoEncontradoException.class, () -> buscarClubeService.buscarClubePorId(id)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Clube com id " + id + " não encontrado.", exception.getMessage());
        Mockito.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }
}
