package br.com.meli.teamcubation_partidas_de_futebol.partida.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.BuscarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.util.EstadioUtil;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.*;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaUtil;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaValidator;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CriarPartidaServiceTest {
    CriarPartidaService criarPartidaService;
    PartidaRepository partidaRepository;
    BuscarEstadioService buscarEstadioService;
    BuscarClubeService buscarClubeService;
    PartidaValidator partidaValidator;

    @BeforeEach
    void setUp(TestInfo  testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        partidaRepository = Mockito.mock(PartidaRepository.class);
        buscarEstadioService = Mockito.mock(BuscarEstadioService.class);
        buscarClubeService = Mockito.mock(BuscarClubeService.class);
        partidaValidator = Mockito.mock(PartidaValidator.class);
        criarPartidaService = new CriarPartidaService(partidaRepository,buscarEstadioService,buscarClubeService,partidaValidator);
    }

    @Test
    void deveCriarUmaPartidaComSucesso(){
        Long partidaId = 1L;
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        int golsMandante = 2;
        int golsVisitante = 2;
        LocalDateTime dataHoraPartida = LocalDateTime.now();
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);
        CriarPartidaRequestDTO criarDTO = new CriarPartidaRequestDTO(clubeMandanteId,clubeVisitanteId,estadioId,golsMandante,golsVisitante,dataHoraPartida);
        Partida partida = PartidaUtil.criarPartida();
        partida.setId(partidaId);

        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doNothing().when(partidaValidator)
                .validarCriacaoDePartidas(clubeMandante, clubeVisitante, estadio, criarDTO);
        Mockito.when(partidaRepository.save(Mockito.any(Partida.class))).thenReturn(partida);

        Partida partidaCriada = criarPartidaService.criarPartida(criarDTO);

        Assertions.assertNotNull(partidaCriada);
        Assertions.assertEquals(partida, partidaCriada);
        Assertions.assertEquals(partidaId, partidaCriada.getId());
        Assertions.assertEquals(partida.getEstadio(), partidaCriada.getEstadio());
        Assertions.assertEquals(partida.getClubeMandante(), partidaCriada.getClubeMandante());
        Assertions.assertEquals(partida.getClubeVisitante(), partidaCriada.getClubeVisitante());
        Assertions.assertEquals(partida.getGolsMandante(), partidaCriada.getGolsMandante());
        Assertions.assertEquals(partida.getGolsVisitante(), partidaCriada.getGolsVisitante());
        Assertions.assertEquals(partida.getDataHora(), partidaCriada.getDataHora());
        Assertions.assertEquals(partida.getDataAtualizacao(), partidaCriada.getDataAtualizacao());
        Assertions.assertEquals(partida.getResultado(), partidaCriada.getResultado());
        Assertions.assertEquals(partida.isGoleada(), partidaCriada.isGoleada());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, buscarEstadioService, partidaValidator, partidaRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService, Mockito.times(1)).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator, Mockito.times(1)).validarCriacaoDePartidas(clubeMandante, clubeVisitante, estadio, criarDTO);
        inOrder.verify(partidaRepository, Mockito.times(1)).save(Mockito.any(Partida.class));
    }

    @Test
    void deveLancarClubeNaoEncontradoException_quandoMandanteNaoExistir() {
        Long clubeMandanteId = 999L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        CriarPartidaRequestDTO criarDTO = new CriarPartidaRequestDTO(clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now());

        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId))
                .thenThrow(new ClubeNaoEncontradoException(clubeMandanteId));

        ClubeNaoEncontradoException exception = Assertions.assertThrows(
                ClubeNaoEncontradoException.class,
                () -> criarPartidaService.criarPartida(criarDTO)
        );

        Assertions.assertEquals("Clube com id " + clubeMandanteId + " não encontrado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, buscarEstadioService, partidaValidator, partidaRepository);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeMandanteId);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarClubeNaoEncontradoException_quandoVisitanteNaoExistir() {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 999L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        CriarPartidaRequestDTO criarDTO = new CriarPartidaRequestDTO(clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now());

        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId))
                .thenThrow(new ClubeNaoEncontradoException(clubeVisitanteId));

        ClubeNaoEncontradoException exception = Assertions.assertThrows(
                ClubeNaoEncontradoException.class,
                () -> criarPartidaService.criarPartida(criarDTO)
        );

        Assertions.assertEquals("Clube com id " + clubeVisitanteId + " não encontrado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeVisitanteId);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarEstadioNaoEncontradoException_quandoEstadioNaoExistir() {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 999L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        CriarPartidaRequestDTO criarDTO = new CriarPartidaRequestDTO(clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now());

        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId))
                .thenThrow(new EstadioNaoEncontradoException(estadioId));

        EstadioNaoEncontradoException exception = Assertions.assertThrows(
                EstadioNaoEncontradoException.class,
                () -> criarPartidaService.criarPartida(criarDTO)
        );

        Assertions.assertEquals("Estadio com id " + estadioId + " não encontrado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, buscarEstadioService);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService, Mockito.times(1)).buscarEstadioPorId(estadioId);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarClubesIguaisException_quandoMandanteEVisitanteForemOMesmoTime() {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 1L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);
        CriarPartidaRequestDTO criarDTO = new CriarPartidaRequestDTO(clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now());

        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doThrow(new ClubesIguaisException())
                .when(partidaValidator)
                .validarCriacaoDePartidas(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        ClubesIguaisException exception = Assertions.assertThrows(
                ClubesIguaisException.class,
                () -> criarPartidaService.criarPartida(criarDTO)
        );

        Assertions.assertEquals("Não é possivel criar a partida pois os clubes são iguais.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, buscarEstadioService, partidaValidator);
        inOrder.verify(buscarClubeService, Mockito.times(2)).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarEstadioService, Mockito.times(1)).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator, Mockito.times(1)).validarCriacaoDePartidas(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarDataPartidaAnteriorACriacaoDoClubeException_quandoCriarPartidaAntesDaCriacaoDeUmClube() {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);

        LocalDateTime dataPartida = LocalDateTime.of(2024,12,15, 20, 0, 0);
        LocalDate dataCriacaoDoClubeExemplo = LocalDate.of(2025,2,22);

        CriarPartidaRequestDTO criarDTO = new CriarPartidaRequestDTO(clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, dataPartida);

        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doThrow(new DataPartidaAnteriorACriacaoDoClubeException())
                .when(partidaValidator)
                .validarCriacaoDePartidas(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        DataPartidaAnteriorACriacaoDoClubeException exception = Assertions.assertThrows(
                DataPartidaAnteriorACriacaoDoClubeException.class,
                () -> criarPartidaService.criarPartida(criarDTO)
        );

        Assertions.assertEquals("Não pode cadastrar uma partida para uma data anterior à data de criação do clube.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, buscarEstadioService, partidaValidator);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService, Mockito.times(1)).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator, Mockito.times(1)).validarCriacaoDePartidas(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarClubeInativoException_quandoUmClubeEstiverInativo() {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        clubeMandante.setAtivo(false);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);

        CriarPartidaRequestDTO criarDTO = new CriarPartidaRequestDTO(clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now());

        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doThrow(new ClubeInativoException())
                .when(partidaValidator)
                .validarCriacaoDePartidas(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        ClubeInativoException exception = Assertions.assertThrows(
                ClubeInativoException.class,
                () -> criarPartidaService.criarPartida(criarDTO)
        );

        Assertions.assertFalse(clubeMandante.getAtivo());
        Assertions.assertEquals("Não é possivel criar a partida pois há um clube inativo", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, buscarEstadioService, partidaValidator);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator).validarCriacaoDePartidas(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarClubesComPartidasEmHorarioMenorQue48HorasException_quandoPartidaForMarcadaComIntervaloMenorQue48Horas() {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);

        CriarPartidaRequestDTO criarDTO = new CriarPartidaRequestDTO(clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now());

        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doThrow(new ClubesComPartidasEmHorarioMenorQue48HorasException())
                .when(partidaValidator)
                .validarCriacaoDePartidas(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        ClubesComPartidasEmHorarioMenorQue48HorasException exception = Assertions.assertThrows(
                ClubesComPartidasEmHorarioMenorQue48HorasException.class,
                () -> criarPartidaService.criarPartida(criarDTO)
        );

        Assertions.assertEquals("Não é possível criar a partida pois um dos clubes já possui uma partida cadastrada em menos de 48 horas desta data.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, buscarEstadioService, partidaValidator);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator).validarCriacaoDePartidas(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarEstadioJaPossuiPartidaNoMesmoDiaException_quandoEstadioJaEstiverOcupadoNoMesmoDia() {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);

        CriarPartidaRequestDTO criarDTO = new CriarPartidaRequestDTO(clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now());

        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doThrow(new EstadioJaPossuiPartidaNoMesmoDiaException())
                .when(partidaValidator)
                .validarCriacaoDePartidas(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        EstadioJaPossuiPartidaNoMesmoDiaException exception = Assertions.assertThrows(
                EstadioJaPossuiPartidaNoMesmoDiaException.class,
                () -> criarPartidaService.criarPartida(criarDTO)
        );

        Assertions.assertEquals("Não é possivel criar a partida pois no estádio já tem uma partida marcada para o mesmo dia", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarClubeService, buscarEstadioService, partidaValidator);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator).validarCriacaoDePartidas(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }
}
