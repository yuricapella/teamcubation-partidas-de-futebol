package br.com.meli.teamcubation_partidas_de_futebol.partida.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.BuscarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.util.EstadioUtil;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.AtualizarPartidaRequestDTO;
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

import java.time.LocalDateTime;

public class AtualizarPartidaServiceTest {
    AtualizarPartidaService atualizarPartidaService;
    PartidaRepository partidaRepository;
    BuscarEstadioService buscarEstadioService;
    BuscarClubeService buscarClubeService;
    BuscarPartidaService buscarPartidaService;
    PartidaValidator partidaValidator;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        partidaRepository = Mockito.mock(PartidaRepository.class);
        buscarEstadioService = Mockito.mock(BuscarEstadioService.class);
        buscarClubeService = Mockito.mock(BuscarClubeService.class);
        buscarPartidaService = Mockito.mock(BuscarPartidaService.class);
        partidaValidator = Mockito.mock(PartidaValidator.class);
        atualizarPartidaService = new AtualizarPartidaService(partidaRepository, buscarEstadioService, buscarClubeService, buscarPartidaService, partidaValidator);
    }

    @Test
    void deveAtualizarUmaPartidaComSucesso() {
        Long partidaId = 1L;
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        int golsMandante = 3;
        int golsVisitante = 2;
        LocalDateTime dataHoraPartida = LocalDateTime.now();
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);
        Partida partidaAntiga = PartidaUtil.criarPartida();
        partidaAntiga.setId(partidaId);

        AtualizarPartidaRequestDTO atualizarDTO = new AtualizarPartidaRequestDTO(
                clubeMandanteId, clubeVisitanteId, estadioId, golsMandante, golsVisitante, dataHoraPartida
        );
        Partida partidaAtualizada = PartidaUtil.criarPartida();
        partidaAtualizada.setId(partidaId);

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId)).thenReturn(partidaAntiga);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doNothing().when(partidaValidator)
                .validarAtualizacaoDePartidas(partidaAntiga, atualizarDTO, clubeMandante, clubeVisitante, estadio);
        Mockito.when(partidaRepository.save(Mockito.any(Partida.class))).thenReturn(partidaAtualizada);

        Partida resultado = atualizarPartidaService.atualizarPartida(atualizarDTO, partidaId);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(partidaId, resultado.getId());
        Assertions.assertEquals(partidaAtualizada.getClubeMandante(), resultado.getClubeMandante());
        Assertions.assertEquals(partidaAtualizada.getClubeVisitante(), resultado.getClubeVisitante());
        Assertions.assertEquals(partidaAtualizada.getEstadio(), resultado.getEstadio());
        Assertions.assertEquals(partidaAtualizada.getGolsMandante(), resultado.getGolsMandante());
        Assertions.assertEquals(partidaAtualizada.getGolsVisitante(), resultado.getGolsVisitante());
        Assertions.assertEquals(partidaAtualizada.getDataHora(), resultado.getDataHora());

        InOrder inOrder = Mockito.inOrder(
                buscarPartidaService,
                buscarClubeService,
                buscarEstadioService,
                partidaValidator,
                partidaRepository
        );
        inOrder.verify(buscarPartidaService, Mockito.times(1)).buscarPartidaPorId(partidaId);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService, Mockito.times(1)).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator, Mockito.times(1)).validarAtualizacaoDePartidas(partidaAntiga, atualizarDTO, clubeMandante, clubeVisitante, estadio);
        inOrder.verify(partidaRepository, Mockito.times(1)).save(Mockito.any(Partida.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void deveLancarPartidaNaoEncontradaException_quandoPartidaNaoExistir() {
        Long partidaId = 999L;
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        AtualizarPartidaRequestDTO atualizarDTO = new AtualizarPartidaRequestDTO(
                clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now()
        );

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId))
                .thenThrow(new PartidaNaoEncontradaException(partidaId));

        PartidaNaoEncontradaException exception = Assertions.assertThrows(
                PartidaNaoEncontradaException.class,
                () -> atualizarPartidaService.atualizarPartida(atualizarDTO, partidaId)
        );

        Assertions.assertEquals("Partida com id " + partidaId + " não encontrada.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(buscarPartidaService);
        inOrder.verify(buscarPartidaService).buscarPartidaPorId(partidaId);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarClubeNaoEncontradoException_quandoMandanteNaoExistir_aoAtualizarPartida() {
        Long partidaId = 1L;
        Long clubeMandanteId = 999L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        AtualizarPartidaRequestDTO atualizarDTO = new AtualizarPartidaRequestDTO(
                clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now()
        );
        Partida partidaAntiga = PartidaUtil.criarPartida();
        partidaAntiga.setId(partidaId);

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId)).thenReturn(partidaAntiga);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId))
                .thenThrow(new ClubeNaoEncontradoException(clubeMandanteId));

        ClubeNaoEncontradoException exception = Assertions.assertThrows(
                ClubeNaoEncontradoException.class,
                () -> atualizarPartidaService.atualizarPartida(atualizarDTO, partidaId)
        );

        Assertions.assertEquals("Clube com id " + clubeMandanteId + " não encontrado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(
                buscarPartidaService,
                buscarClubeService
        );
        inOrder.verify(buscarPartidaService, Mockito.times(1)).buscarPartidaPorId(partidaId);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeMandanteId);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarClubeNaoEncontradoException_quandoVisitanteNaoExistir_aoAtualizarPartida() {
        Long partidaId = 1L;
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 999L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Partida partidaAntiga = PartidaUtil.criarPartida();
        partidaAntiga.setId(partidaId);
        AtualizarPartidaRequestDTO atualizarDTO = new AtualizarPartidaRequestDTO(
                clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now()
        );

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId)).thenReturn(partidaAntiga);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId))
                .thenThrow(new ClubeNaoEncontradoException(clubeVisitanteId));

        ClubeNaoEncontradoException exception = Assertions.assertThrows(
                ClubeNaoEncontradoException.class,
                () -> atualizarPartidaService.atualizarPartida(atualizarDTO, partidaId)
        );

        Assertions.assertEquals("Clube com id " + clubeVisitanteId + " não encontrado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(
                buscarPartidaService,
                buscarClubeService
        );
        inOrder.verify(buscarPartidaService, Mockito.times(1)).buscarPartidaPorId(partidaId);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(clubeVisitanteId);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarEstadioNaoEncontradoException_quandoEstadioNaoExistir_aoAtualizarPartida() {
        Long partidaId = 1L;
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 999L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Partida partidaAntiga = PartidaUtil.criarPartida();
        partidaAntiga.setId(partidaId);

        AtualizarPartidaRequestDTO atualizarDTO = new AtualizarPartidaRequestDTO(
                clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now()
        );

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId)).thenReturn(partidaAntiga);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId))
                .thenThrow(new EstadioNaoEncontradoException(estadioId));

        EstadioNaoEncontradoException exception = Assertions.assertThrows(
                EstadioNaoEncontradoException.class,
                () -> atualizarPartidaService.atualizarPartida(atualizarDTO, partidaId)
        );

        Assertions.assertEquals("Estadio com id " + estadioId + " não encontrado.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(
                buscarPartidaService,
                buscarClubeService,
                buscarEstadioService
        );
        inOrder.verify(buscarPartidaService).buscarPartidaPorId(partidaId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService).buscarEstadioPorId(estadioId);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarClubesIguaisException_quandoMandanteEVisitanteForemOMesmoTime_aoAtualizarPartida() {
        Long partidaId = 1L;
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 1L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);
        Partida partidaAntiga = PartidaUtil.criarPartida();
        partidaAntiga.setId(partidaId);

        AtualizarPartidaRequestDTO atualizarDTO = new AtualizarPartidaRequestDTO(
                clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now()
        );

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId)).thenReturn(partidaAntiga);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doThrow(new ClubesIguaisException())
                .when(partidaValidator)
                .validarAtualizacaoDePartidas(
                        Mockito.any(Partida.class),
                        Mockito.any(AtualizarPartidaRequestDTO.class),
                        Mockito.any(Clube.class),
                        Mockito.any(Clube.class),
                        Mockito.any(Estadio.class));

        ClubesIguaisException exception = Assertions.assertThrows(
                ClubesIguaisException.class,
                () -> atualizarPartidaService.atualizarPartida(atualizarDTO, partidaId)
        );

        Assertions.assertEquals("Não é possivel criar a partida pois os clubes são iguais.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(
                buscarPartidaService,
                buscarClubeService,
                buscarEstadioService,
                partidaValidator
        );
        inOrder.verify(buscarPartidaService).buscarPartidaPorId(partidaId);
        inOrder.verify(buscarClubeService, Mockito.times(2)).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarEstadioService).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator).validarAtualizacaoDePartidas(
                Mockito.any(Partida.class),
                Mockito.any(AtualizarPartidaRequestDTO.class),
                Mockito.any(Clube.class),
                Mockito.any(Clube.class),
                Mockito.any(Estadio.class));
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarDataPartidaAnteriorACriacaoDoClubeException_quandoAtualizarPartidaAntesDaCriacaoDeUmClube() {
        Long partidaId = 1L;
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);
        Partida partidaAntiga = PartidaUtil.criarPartida();
        partidaAntiga.setId(partidaId);

        LocalDateTime dataPartida = LocalDateTime.of(2024,12,15, 20, 0, 0);

        AtualizarPartidaRequestDTO atualizarDTO = new AtualizarPartidaRequestDTO(
                clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, dataPartida
        );

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId)).thenReturn(partidaAntiga);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doThrow(new DataPartidaAnteriorACriacaoDoClubeException())
                .when(partidaValidator)
                .validarAtualizacaoDePartidas(partidaAntiga, atualizarDTO, clubeMandante, clubeVisitante, estadio);

        DataPartidaAnteriorACriacaoDoClubeException exception = Assertions.assertThrows(
                DataPartidaAnteriorACriacaoDoClubeException.class,
                () -> atualizarPartidaService.atualizarPartida(atualizarDTO, partidaId)
        );

        Assertions.assertEquals("Não pode cadastrar uma partida para uma data anterior à data de criação do clube.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(
                buscarPartidaService,
                buscarClubeService,
                buscarEstadioService,
                partidaValidator
        );
        inOrder.verify(buscarPartidaService).buscarPartidaPorId(partidaId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator).validarAtualizacaoDePartidas(partidaAntiga, atualizarDTO, clubeMandante, clubeVisitante, estadio);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }


    @Test
    void deveLancarClubeInativoException_quandoUmClubeEstiverInativo_aoAtualizarPartida() {
        Long partidaId = 1L;
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        clubeMandante.setAtivo(false);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);
        Partida partidaAntiga = PartidaUtil.criarPartida();
        partidaAntiga.setId(partidaId);

        AtualizarPartidaRequestDTO atualizarDTO = new AtualizarPartidaRequestDTO(
                clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now()
        );

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId)).thenReturn(partidaAntiga);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doThrow(new ClubeInativoException())
                .when(partidaValidator)
                .validarAtualizacaoDePartidas(partidaAntiga, atualizarDTO, clubeMandante, clubeVisitante, estadio);

        ClubeInativoException exception = Assertions.assertThrows(
                ClubeInativoException.class,
                () -> atualizarPartidaService.atualizarPartida(atualizarDTO, partidaId)
        );

        Assertions.assertFalse(clubeMandante.getAtivo());
        Assertions.assertEquals("Não é possivel criar a partida pois há um clube inativo", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(
                buscarPartidaService,
                buscarClubeService,
                buscarEstadioService,
                partidaValidator
        );
        inOrder.verify(buscarPartidaService).buscarPartidaPorId(partidaId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator).validarAtualizacaoDePartidas(partidaAntiga, atualizarDTO, clubeMandante, clubeVisitante, estadio);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarClubesComPartidasEmHorarioMenorQue48HorasException_quandoPartidaForMarcadaComIntervaloMenorQue48Horas_aoAtualizarPartida() {
        Long partidaId = 1L;
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);
        Partida partidaAntiga = PartidaUtil.criarPartida();
        partidaAntiga.setId(partidaId);

        AtualizarPartidaRequestDTO atualizarDTO = new AtualizarPartidaRequestDTO(
                clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now()
        );

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId)).thenReturn(partidaAntiga);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doThrow(new ClubesComPartidasEmHorarioMenorQue48HorasException())
                .when(partidaValidator)
                .validarAtualizacaoDePartidas(partidaAntiga, atualizarDTO, clubeMandante, clubeVisitante, estadio);

        ClubesComPartidasEmHorarioMenorQue48HorasException exception = Assertions.assertThrows(
                ClubesComPartidasEmHorarioMenorQue48HorasException.class,
                () -> atualizarPartidaService.atualizarPartida(atualizarDTO, partidaId)
        );

        Assertions.assertEquals("Não é possível criar a partida pois um dos clubes já possui uma partida cadastrada em menos de 48 horas desta data.", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(
                buscarPartidaService,
                buscarClubeService,
                buscarEstadioService,
                partidaValidator
        );
        inOrder.verify(buscarPartidaService).buscarPartidaPorId(partidaId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator).validarAtualizacaoDePartidas(partidaAntiga, atualizarDTO, clubeMandante, clubeVisitante, estadio);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }

    @Test
    void deveLancarEstadioJaPossuiPartidaNoMesmoDiaException_quandoEstadioJaEstiverOcupadoNoMesmoDia_aoAtualizarPartida() {
        Long partidaId = 1L;
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        Clube clubeMandante = ClubeUtil.criarClube(clubeMandanteId);
        Clube clubeVisitante = ClubeUtil.criarClube(clubeVisitanteId);
        Estadio estadio = EstadioUtil.criarEstadio(estadioId);
        Partida partidaAntiga = PartidaUtil.criarPartida();
        partidaAntiga.setId(partidaId);

        AtualizarPartidaRequestDTO atualizarDTO = new AtualizarPartidaRequestDTO(
                clubeMandanteId, clubeVisitanteId, estadioId, 2, 2, LocalDateTime.now()
        );

        Mockito.when(buscarPartidaService.buscarPartidaPorId(partidaId)).thenReturn(partidaAntiga);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeMandanteId)).thenReturn(clubeMandante);
        Mockito.when(buscarClubeService.buscarClubePorId(clubeVisitanteId)).thenReturn(clubeVisitante);
        Mockito.when(buscarEstadioService.buscarEstadioPorId(estadioId)).thenReturn(estadio);
        Mockito.doThrow(new EstadioJaPossuiPartidaNoMesmoDiaException())
                .when(partidaValidator)
                .validarAtualizacaoDePartidas(partidaAntiga, atualizarDTO, clubeMandante, clubeVisitante, estadio);

        EstadioJaPossuiPartidaNoMesmoDiaException exception = Assertions.assertThrows(
                EstadioJaPossuiPartidaNoMesmoDiaException.class,
                () -> atualizarPartidaService.atualizarPartida(atualizarDTO, partidaId)
        );

        Assertions.assertEquals("Não é possivel criar a partida pois no estádio já tem uma partida marcada para o mesmo dia", exception.getMessage());

        InOrder inOrder = Mockito.inOrder(
                buscarPartidaService,
                buscarClubeService,
                buscarEstadioService,
                partidaValidator
        );
        inOrder.verify(buscarPartidaService).buscarPartidaPorId(partidaId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeMandanteId);
        inOrder.verify(buscarClubeService).buscarClubePorId(clubeVisitanteId);
        inOrder.verify(buscarEstadioService).buscarEstadioPorId(estadioId);
        inOrder.verify(partidaValidator).validarAtualizacaoDePartidas(partidaAntiga, atualizarDTO, clubeMandante, clubeVisitante, estadio);
        inOrder.verifyNoMoreInteractions();

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }
}
