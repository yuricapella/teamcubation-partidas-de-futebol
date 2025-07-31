package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.AtualizarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeComNomeJaCadastradoNoEstadoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.DataCriacaoPosteriorDataPartidaException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.EstadoInexistenteException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeValidator;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.time.LocalDate;

public class AtualizarClubeServiceTest {
    AtualizarClubeService atualizarClubeService;
    BuscarClubeService buscarClubeService;
    ClubeRepository clubeRepository;
    ClubeValidator clubeValidator;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        buscarClubeService = Mockito.mock(BuscarClubeService.class);
        clubeRepository = Mockito.mock(ClubeRepository.class);
        clubeValidator = Mockito.mock(ClubeValidator.class);
        atualizarClubeService = new AtualizarClubeService(buscarClubeService, clubeRepository,clubeValidator);
    }

    @Test
    void deveAtualizarUmClubePorIdComSucesso(){
        Long id = 1L;
        AtualizarClubeRequestDTO atualizarDTO = new AtualizarClubeRequestDTO("clube de time atualizado","AM",LocalDate.of(2025,11,3));
        Clube clube = new Clube("clube de time","AM",true, LocalDate.of(2025,11,3));
        clube.setId(id);

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenReturn(clube);
        Mockito.doNothing().when(clubeValidator).validarClubeNaAtualizacao(Mockito.any(Clube.class));
        Mockito.when(clubeRepository.save(Mockito.any(Clube.class))).thenReturn(clube);

        Clube clubeAtualizado = atualizarClubeService.atualizar(atualizarDTO,id);

        Assertions.assertNotNull(clubeAtualizado);
        Assertions.assertEquals(clubeAtualizado.getNome(), clube.getNome());
        Assertions.assertEquals(clubeAtualizado.getSiglaEstado(), clube.getSiglaEstado());
        Assertions.assertEquals(clubeAtualizado.getDataCriacao(), clube.getDataCriacao());
        Assertions.assertNotNull(clubeAtualizado.getId());
        Assertions.assertTrue(clubeAtualizado.getAtivo());
        Assertions.assertNotNull(clubeAtualizado.getDataAtualizacao());

        InOrder inOrder = Mockito.inOrder(buscarClubeService,clubeValidator, clubeRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
        inOrder.verify(clubeValidator, Mockito.times(1)).validarClubeNaAtualizacao(Mockito.any(Clube.class));
        inOrder.verify(clubeRepository, Mockito.times(1)).save(Mockito.any(Clube.class));
    }

    @Test
    void deveLancarClubeNaoEncontradoException_quandoClubeNaoExistir(){
        Long id = 1L;
        AtualizarClubeRequestDTO atualizarDTO = new AtualizarClubeRequestDTO("clube de time atualizado", "AM", LocalDate.of(2025,11,3));

        Mockito.doThrow(new ClubeNaoEncontradoException(id))
                .when(buscarClubeService).buscarClubePorId(Mockito.any(Long.class));

        ClubeNaoEncontradoException exception = Assertions.assertThrows(
                ClubeNaoEncontradoException.class,
                () -> atualizarClubeService.atualizar(atualizarDTO,id)
        );

        Assertions.assertEquals("Clube com id 1 não encontrado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService,clubeValidator,clubeRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
        inOrder.verify(clubeValidator, Mockito.never()).validarClubeNaAtualizacao(Mockito.any(Clube.class));
        inOrder.verify(clubeRepository, Mockito.never()).save(Mockito.any(Clube.class));

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarEstadoInexistenteException_quandoEstadoNaoExistir(){
        Long id = 1L;
        String estadoInvalido = "XX";
        AtualizarClubeRequestDTO atualizarDTO = new AtualizarClubeRequestDTO("clube de time atualizado", estadoInvalido, LocalDate.of(2025,11,3));

        Clube clube = ClubeUtil.criarClube(id);

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenReturn(clube);
        Mockito.doThrow(new EstadoInexistenteException(estadoInvalido))
                .when(clubeValidator).validarClubeNaAtualizacao(Mockito.any(Clube.class));

        EstadoInexistenteException exception = Assertions.assertThrows(
                EstadoInexistenteException.class,
                () -> atualizarClubeService.atualizar(atualizarDTO,id)
        );

        Assertions.assertEquals("Não é possivel criar o clube pois o estado XX não existe.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService,clubeValidator,clubeRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
        inOrder.verify(clubeValidator, Mockito.times(1)).validarClubeNaAtualizacao(Mockito.any(Clube.class));
        inOrder.verify(clubeRepository, Mockito.never()).save(Mockito.any(Clube.class));

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarClubeComNomeJaCadastradoNoEstadoException_quandoNomeDoClubeJaExisteNoMesmoEstado(){
        Long id = 1L;
        String estadoIgual = "AM";
        String nomeIgual = "clube de time";
        AtualizarClubeRequestDTO atualizarDTO = new AtualizarClubeRequestDTO(nomeIgual, estadoIgual, LocalDate.of(2025,11,3));

        Clube clube = ClubeUtil.criarClube(id);

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenReturn(clube);
        Mockito.doThrow(new ClubeComNomeJaCadastradoNoEstadoException())
                .when(clubeValidator).validarClubeNaAtualizacao(Mockito.any(Clube.class));

        ClubeComNomeJaCadastradoNoEstadoException exception = Assertions.assertThrows(
                ClubeComNomeJaCadastradoNoEstadoException.class,
                () -> atualizarClubeService.atualizar(atualizarDTO,id)
        );

        Assertions.assertEquals("Já existe clube com este nome no mesmo estado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService,clubeValidator,clubeRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
        inOrder.verify(clubeValidator, Mockito.times(1)).validarClubeNaAtualizacao(Mockito.any(Clube.class));
        inOrder.verify(clubeRepository, Mockito.never()).save(Mockito.any(Clube.class));

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarDataCriacaoPosteriorDataPartidaException_quandoAlterarDataCriacaoPosteriorADataPartida(){
        Long id = 1L;
        String nome = "clube de time";
        String estado = "AM";
        LocalDate dataPosteriorADataPartida = LocalDate.of(2025,12,3);
        AtualizarClubeRequestDTO atualizarDTO = new AtualizarClubeRequestDTO(nome, estado, dataPosteriorADataPartida);

        Clube clube = ClubeUtil.criarClube(id);

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenReturn(clube);
        Mockito.doThrow(new DataCriacaoPosteriorDataPartidaException(id))
                .when(clubeValidator).validarClubeNaAtualizacao(Mockito.any(Clube.class));

        DataCriacaoPosteriorDataPartidaException exception = Assertions.assertThrows(
                DataCriacaoPosteriorDataPartidaException.class,
                () -> atualizarClubeService.atualizar(atualizarDTO,id)
        );

        Assertions.assertEquals("A data de criação do clube com id 1 está posterior a data de uma partida cadastrada.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService,clubeValidator,clubeRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
        inOrder.verify(clubeValidator, Mockito.times(1)).validarClubeNaAtualizacao(Mockito.any(Clube.class));
        inOrder.verify(clubeRepository, Mockito.never()).save(Mockito.any(Clube.class));

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }


}
