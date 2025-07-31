//package br.com.meli.teamcubation_partidas_de_futebol.estadio.service;
//
//import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.AtualizarEstadioRequestDTO;
//import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioJaExisteException;
//import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioNaoEncontradoException;
//import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
//import br.com.meli.teamcubation_partidas_de_futebol.estadio.repository.EstadioRepository;
//import br.com.meli.teamcubation_partidas_de_futebol.estadio.util.EstadioValidator;
//import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInfo;
//import org.mockito.InOrder;
//import org.mockito.Mockito;
//
//public class AtualizarEstadioServiceTest {
//    AtualizarEstadioService atualizarEstadioService;
//    EstadioRepository estadioRepository;
//    EstadioValidator estadioValidator;
//    BuscarEstadioService buscarEstadioService;
//    EnderecoViaCepClient enderecoViaCepClient;
//
//    @BeforeEach
//    void setUp(TestInfo testInfo) {
//        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
//        estadioRepository = Mockito.mock(EstadioRepository.class);
//        estadioValidator = Mockito.mock(EstadioValidator.class);
//        buscarEstadioService = Mockito.mock(BuscarEstadioService.class);
//        enderecoViaCepClient = Mockito.mock(EnderecoViaCepClient.class);
//        atualizarEstadioService = new AtualizarEstadioService(estadioRepository,estadioValidator, buscarEstadioService, enderecoViaCepClient);
//    }
//
//    @Test
//    void deveAtualizarUmEstadioComSucesso(){
//        Long id = 1L;
//        AtualizarEstadioRequestDTO atualizarDTO = new AtualizarEstadioRequestDTO("Estadio de time atualizado");
//        Estadio estadio = new Estadio("Estadio de time atualizado");
//        estadio.setId(id);
//
//        Mockito.when(buscarEstadioService.buscarEstadioPorId(id)).thenReturn(estadio);
//        Mockito.doNothing().when(estadioValidator).validarDadosDoEstadioAoAtualizar(Mockito.any(String.class), Mockito.any(String.class));
//        Mockito.when(estadioRepository.save(Mockito.any(Estadio.class))).thenReturn(estadio);
//
//        Estadio estadioAtualizado = atualizarEstadioService.atualizarEstadio(atualizarDTO,id);
//
//        Assertions.assertNotNull(estadioAtualizado);
//        Assertions.assertEquals(atualizarDTO.getNome(), estadioAtualizado.getNome());
//        Assertions.assertNotNull(estadioAtualizado.getId());
//        Assertions.assertNotNull(estadioAtualizado.getDataAtualizacao());
//
//        InOrder inOrder = Mockito.inOrder(buscarEstadioService,estadioValidator, estadioRepository);
//        inOrder.verify(buscarEstadioService, Mockito.times(1)).buscarEstadioPorId(id);
//        inOrder.verify(estadioValidator, Mockito.times(1)).validarDadosDoEstadioAoAtualizar(Mockito.any(String.class), Mockito.any(String.class));
//        inOrder.verify(estadioRepository, Mockito.times(1)).save(Mockito.any(Estadio.class));
//    }
//
//    @Test
//    void deveLancarEstadioNaoEncontradoException_quandoAtualizarEstadioInexistente(){
//        Long id = 1L;
//        AtualizarEstadioRequestDTO atualizarDTO = new AtualizarEstadioRequestDTO("Estadio de time atualizado");
//
//        Mockito.doThrow(new EstadioNaoEncontradoException(id))
//                .when(buscarEstadioService).buscarEstadioPorId(Mockito.any(Long.class));
//
//        EstadioNaoEncontradoException exception = Assertions.assertThrows(
//                EstadioNaoEncontradoException.class,
//                () -> atualizarEstadioService.atualizarEstadio(atualizarDTO,id)
//        );
//
//        Assertions.assertEquals("Estadio com id 1 não encontrado.", exception.getMessage());
//
//        InOrder inOrder = Mockito.inOrder(buscarEstadioService,estadioValidator, estadioRepository);
//        inOrder.verify(buscarEstadioService, Mockito.times(1)).buscarEstadioPorId(id);
//        inOrder.verify(estadioValidator, Mockito.never()).validarDadosDoEstadioAoAtualizar(Mockito.any(String.class), Mockito.any(String.class));
//        inOrder.verify(estadioRepository, Mockito.never()).save(Mockito.any(Estadio.class));
//
//        PrintUtil.printMensagemDeErro(exception.getMessage());
//    }
//
//    @Test
//    void deveLancarEstadioJaExisteException_quandoTentarAtualizarEstadioComMesmoNome(){
//        Long id = 1L;
//        String nomeDuplicado = "Estadio de time atualizado";
//        AtualizarEstadioRequestDTO atualizarDTO = new AtualizarEstadioRequestDTO(nomeDuplicado);
//
//        Estadio estadio = new Estadio("Estadio de time atualizado");
//
//        Mockito.when(buscarEstadioService.buscarEstadioPorId(id)).thenReturn(estadio);
//        Mockito.doThrow(new EstadioJaExisteException())
//                .when(estadioValidator).validarDadosDoEstadioAoAtualizar(Mockito.any(String.class), Mockito.any(String.class));
//
//        EstadioJaExisteException exception = Assertions.assertThrows(
//                EstadioJaExisteException.class,
//                () -> atualizarEstadioService.atualizarEstadio(atualizarDTO,id)
//        );
//
//        Assertions.assertEquals("Já existe um estadio com este nome.", exception.getMessage());
//
//        InOrder inOrder = Mockito.inOrder(buscarEstadioService,estadioValidator, estadioRepository);
//        inOrder.verify(buscarEstadioService, Mockito.times(1)).buscarEstadioPorId(id);
//        inOrder.verify(estadioValidator, Mockito.times(1)).validarDadosDoEstadioAoAtualizar(Mockito.any(String.class), Mockito.any(String.class));
//        inOrder.verify(estadioRepository, Mockito.never()).save(Mockito.any(Estadio.class));
//
//        PrintUtil.printMensagemDeErro(exception.getMessage());
//    }
//}
