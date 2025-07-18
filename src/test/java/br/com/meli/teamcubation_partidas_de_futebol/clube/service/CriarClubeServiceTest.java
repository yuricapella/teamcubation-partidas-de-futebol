package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.CriarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeComNomeJaCadastradoNoEstadoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.EstadoInexistenteException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeValidator;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.time.LocalDate;

public class CriarClubeServiceTest {
    CriarClubeService criarClubeService;
    ClubeRepository clubeRepository;
    ClubeValidator clubeValidator;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        clubeRepository = Mockito.mock(ClubeRepository.class);
        clubeValidator = Mockito.mock(ClubeValidator.class);
        criarClubeService = new CriarClubeService(clubeRepository,clubeValidator);
    }

    @Test
    void deveCriarUmClubeComSucesso(){
        Long id = 1L;
        CriarClubeRequestDTO criarDTO = new CriarClubeRequestDTO("clube de time","AM",LocalDate.of(2025,11,3));
        Clube clube = new Clube("clube de time","AM",true, LocalDate.of(2025,11,3));
        clube.setId(id);

        Mockito.doNothing().when(clubeValidator).validarClubeNaCriacao(Mockito.any(Clube.class));
        Mockito.when(clubeRepository.save(Mockito.any(Clube.class))).thenReturn(clube);

        Clube clubeCriado = criarClubeService.criarClube(criarDTO);

        Assertions.assertNotNull(clubeCriado);
        Assertions.assertEquals(criarDTO.getNome(), clubeCriado.getNome());
        Assertions.assertEquals(criarDTO.getSiglaEstado(), clubeCriado.getSiglaEstado());
        Assertions.assertEquals(criarDTO.getDataCriacao(), clubeCriado.getDataCriacao());
        Assertions.assertNotNull(clubeCriado.getId());
        Assertions.assertTrue(clubeCriado.getAtivo());
        Assertions.assertNull(clubeCriado.getDataAtualizacao());

        InOrder inOrder = Mockito.inOrder(clubeValidator, clubeRepository);
        inOrder.verify(clubeValidator, Mockito.times(1)).validarClubeNaCriacao(Mockito.any(Clube.class));
        inOrder.verify(clubeRepository, Mockito.times(1)).save(Mockito.any(Clube.class));
    }

    @Test
    void deveLancarEstadoInexistenteException_quandoEstadoNaoExistir(){
        String estadoInvalido = "XX";
        CriarClubeRequestDTO criarDTO = new CriarClubeRequestDTO("clube de time", estadoInvalido, LocalDate.of(2025,11,3));

        Mockito.doThrow(new EstadoInexistenteException(estadoInvalido))
                .when(clubeValidator).validarClubeNaCriacao(Mockito.any(Clube.class));

        EstadoInexistenteException exception = Assertions.assertThrows(
                EstadoInexistenteException.class,
                () -> criarClubeService.criarClube(criarDTO)
        );

        Assertions.assertEquals("Não é possivel criar o clube pois o estado XX não existe.", exception.getMessage());

        Mockito.verify(clubeValidator, Mockito.times(1)).validarClubeNaCriacao(Mockito.any(Clube.class));
        Mockito.verify(clubeRepository, Mockito.never()).save(Mockito.any(Clube.class));
    }

    @Test
    void deveLancarClubeComNomeJaCadastradoNoEstadoException_quandoNomeDoClubeJaExisteNoMesmoEstado(){
        String estadoIgual = "AM";
        String nomeIgual = "clube de time";
        CriarClubeRequestDTO criarDTO = new CriarClubeRequestDTO(nomeIgual, estadoIgual, LocalDate.of(2025,11,3));

        Mockito.doThrow(new ClubeComNomeJaCadastradoNoEstadoException())
                .when(clubeValidator).validarClubeNaCriacao(Mockito.any(Clube.class));

        ClubeComNomeJaCadastradoNoEstadoException exception = Assertions.assertThrows(
                ClubeComNomeJaCadastradoNoEstadoException.class,
                () -> criarClubeService.criarClube(criarDTO)
        );

        Assertions.assertEquals("Já existe clube com este nome no mesmo estado.", exception.getMessage());

        Mockito.verify(clubeValidator, Mockito.times(1)).validarClubeNaCriacao(Mockito.any(Clube.class));
        Mockito.verify(clubeRepository, Mockito.never()).save(Mockito.any(Clube.class));
    }


}
