package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.CriarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.CriarClubeRequestMapper;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class CriarClubeServiceTest {
    @InjectMocks
    CriarClubeService criarClubeService;
    @Mock
    ClubeRepository clubeRepository;
    @Mock
    ClubeValidator clubeValidator;
    @Mock
    ModelMapper modelMapper;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
    }

    @Test
    void deveCriarUmClubeComSucesso(){
        Long id = 1L;
        CriarClubeRequestDTO criarDTO = new CriarClubeRequestDTO("clube de time","AM",LocalDate.of(2025,11,3));
        Clube clube = CriarClubeRequestMapper.toEntity(criarDTO);
        clube.setId(id);

        ClubeResponseDTO dtoEsperado = new ClubeResponseDTO();
        dtoEsperado.setNome(clube.getNome());
        dtoEsperado.setSiglaEstado(clube.getSiglaEstado());
        dtoEsperado.setDataCriacao(clube.getDataCriacao());

        Mockito.doNothing().when(clubeValidator).validarClubeNaCriacao(Mockito.any(Clube.class));
        Mockito.when(clubeRepository.save(Mockito.any(Clube.class))).thenReturn(clube);
        Mockito.when(modelMapper.map(Mockito.any(Clube.class), Mockito.eq(ClubeResponseDTO.class))).thenReturn(dtoEsperado);

        ClubeResponseDTO resultado = criarClubeService.criarClube(criarDTO);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(dtoEsperado.getNome(), resultado.getNome());
        Assertions.assertEquals(dtoEsperado.getSiglaEstado(), resultado.getSiglaEstado());
        Assertions.assertEquals(dtoEsperado.getDataCriacao(), resultado.getDataCriacao());
        Assertions.assertNotNull(clube.getId());
        Assertions.assertTrue(clube.getAtivo());
        Assertions.assertNull(clube.getDataAtualizacao());

        InOrder inOrder = Mockito.inOrder(clubeValidator, clubeRepository, modelMapper);
        inOrder.verify(clubeValidator, Mockito.times(1)).validarClubeNaCriacao(Mockito.any(Clube.class));
        inOrder.verify(clubeRepository, Mockito.times(1)).save(Mockito.any(Clube.class));
        inOrder.verify(modelMapper, Mockito.times(1)).map(Mockito.any(Clube.class), Mockito.eq(ClubeResponseDTO.class));
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

        PrintUtil.printMensagemDeErro(exception.getMessage());
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

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }


}
