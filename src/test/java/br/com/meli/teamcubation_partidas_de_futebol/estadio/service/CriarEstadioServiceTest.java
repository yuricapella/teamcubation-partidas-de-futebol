package br.com.meli.teamcubation_partidas_de_futebol.estadio.service;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CriarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioJaExisteException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.repository.EstadioRepository;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.util.EstadioValidator;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class CriarEstadioServiceTest {
    CriarEstadioService criarEstadioService;
    EstadioRepository estadioRepository;
    EstadioValidator estadioValidator;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        estadioRepository = Mockito.mock(EstadioRepository.class);
        estadioValidator = Mockito.mock(EstadioValidator.class);
        criarEstadioService = new CriarEstadioService(estadioRepository,estadioValidator);
    }

    @Test
    void deveCriarUmEstadioComSucesso(){
        Long id = 1L;
        CriarEstadioRequestDTO criarDTO = new CriarEstadioRequestDTO("Estadio de time");
        Estadio estadio = new Estadio("Estadio de time");
        estadio.setId(id);

        Mockito.doNothing().when(estadioValidator).validarDadosDoEstadioAoCriar(Mockito.any(String.class));
        Mockito.when(estadioRepository.save(Mockito.any(Estadio.class))).thenReturn(estadio);

        Estadio estadioCriado = criarEstadioService.criarEstadio(criarDTO);

        Assertions.assertNotNull(estadioCriado);
        Assertions.assertEquals(criarDTO.getNome(), estadioCriado.getNome());
        Assertions.assertNotNull(estadioCriado.getId());
        Assertions.assertNull(estadioCriado.getDataAtualizacao());

        InOrder inOrder = Mockito.inOrder(estadioValidator, estadioRepository);
        inOrder.verify(estadioValidator, Mockito.times(1)).validarDadosDoEstadioAoCriar(Mockito.any(String.class));
        inOrder.verify(estadioRepository, Mockito.times(1)).save(Mockito.any(Estadio.class));
    }

    @Test
    void deveLancarEstadioJaExisteException_quandoTentarCriarEstadioComMesmoNome(){
        String nomeDuplicado = "Estadio de time";
        CriarEstadioRequestDTO criarDTO = new CriarEstadioRequestDTO(nomeDuplicado);

        Mockito.doThrow(new EstadioJaExisteException())
                .when(estadioValidator).validarDadosDoEstadioAoCriar(Mockito.any(String.class));

        EstadioJaExisteException exception = Assertions.assertThrows(
                EstadioJaExisteException.class,
                () -> criarEstadioService.criarEstadio(criarDTO)
        );

        Assertions.assertEquals("Já existe um estadio com este nome.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(estadioValidator, estadioRepository);
        inOrder.verify(estadioValidator, Mockito.times(1)).validarDadosDoEstadioAoCriar(Mockito.any(String.class));
        inOrder.verify(estadioRepository, Mockito.never()).save(Mockito.any(Estadio.class));

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }
}
