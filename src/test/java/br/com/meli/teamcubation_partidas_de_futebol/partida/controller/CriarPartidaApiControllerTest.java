package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.GlobalApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.CriarPartidaRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.*;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.CriarPartidaService;
import br.com.meli.teamcubation_partidas_de_futebol.util.JsonUtil;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CriarPartidaApiControllerTest {
    @InjectMocks
    CriarPartidaApiController criarPartidaApiController;
    @Mock
    CriarPartidaService criarPartidaService;

    MockMvc mockMvc;

    final String PATH = "/api/partida/criar";

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(criarPartidaApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler(), new PartidaApiExceptionHandler())
                .build();
    }

    @Test
    void deveCriarPartidaComSucessoERetornar201Created() throws Exception {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        int golsMandante = 3;
        int golsVisitante = 2;
        LocalDateTime dataPartida = LocalDateTime.now();

        CriarPartidaRequestDTO criarDTO = new CriarPartidaRequestDTO
        (clubeMandanteId,clubeVisitanteId, estadioId, golsMandante, golsVisitante, dataPartida);

        Clube clubeMandante = new Clube();
        clubeMandante.setId(clubeMandanteId);

        Clube clubeVisitante = new Clube();
        clubeVisitante.setId(clubeVisitanteId);

        Estadio estadio = new Estadio();
        estadio.setId(estadioId);

        Partida partidaCriada = new Partida
                (estadio,clubeMandante,clubeVisitante,golsMandante,golsVisitante,dataPartida);

        Mockito.when(criarPartidaService.criarPartida(Mockito.any(CriarPartidaRequestDTO.class)))
                .thenReturn(partidaCriada);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(criarDTO)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarPartidaService, Mockito.times(1))
                .criarPartida
                (Mockito.argThat(dto ->
                    dto.getClubeMandanteId().equals(clubeMandanteId) &&
                    dto.getClubeVisitanteId().equals(clubeVisitanteId) &&
                    dto.getEstadioId().equals(estadioId) &&
                    dto.getGolsMandante() == golsMandante &&
                    dto.getGolsVisitante() == golsVisitante &&
                    dto.getDataHora().equals(dataPartida)));
    }

    @ParameterizedTest
    @CsvSource({
            ",2,3,0,0,2023-01-01T15:00:00,clubeMandanteId,O clube mandante é obrigatório",
            "1,,3,0,0,2023-01-01T15:00:00,clubeVisitanteId,O clube visitante é obrigatório",
            "1,2,,0,0,2023-01-01T15:00:00,estadioId,O estádio é obrigatório",

            "1,2,3,-1,0,2023-01-01T15:00:00,golsMandante,O número de gols do mandante não pode ser negativo",
            "1,2,3,0,-2,2023-01-01T15:00:00,golsVisitante,O número de gols do visitante não pode ser negativo",

            "1,2,3,0,0,,dataHora,A data e hora da partida são obrigatórias",
            "1,2,3,0,0,2099-12-31T23:59:00,dataHora,A data da partida não pode ser futura"
    })
    void deveRetornarBadRequestParaCamposInvalidos(
            Long clubeMandanteId,
            Long clubeVisitanteId,
            Long estadioId,
            int golsMandante,
            int golsVisitante,
            String dataHoraStr,
            String campoErro,
            String mensagemEsperada
    ) throws Exception {
        LocalDateTime dataHora = (dataHoraStr == null || dataHoraStr.isBlank()) ? null : LocalDateTime.parse(dataHoraStr);

        CriarPartidaRequestDTO dtoInvalido =
        new CriarPartidaRequestDTO(clubeMandanteId,clubeVisitanteId,estadioId,golsMandante,golsVisitante,dataHora);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigoErro").value("CAMPO_INVALIDO"))
                .andExpect(jsonPath("$.errors." + campoErro).value(mensagemEsperada))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deveRetornarBadRequestQuandoClubesSaoIguais() throws Exception {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 1L;
        Long estadioId = 1L;
        int golsMandante = 3;
        int golsVisitante = 2;
        LocalDateTime dataPartida = LocalDateTime.now();

        CriarPartidaRequestDTO dto =
        new CriarPartidaRequestDTO(clubeMandanteId,clubeVisitanteId,estadioId,golsMandante,golsVisitante,dataPartida);

        Mockito.when(criarPartidaService.criarPartida(Mockito.any(CriarPartidaRequestDTO.class)))
                .thenThrow(new ClubesIguaisException());

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigoErro").value("CLUBES_IGUAIS"))
                .andExpect(jsonPath("$.mensagem").value("Não é possivel criar a partida pois os clubes são iguais."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarPartidaService, Mockito.times(1))
                .criarPartida(Mockito.any(CriarPartidaRequestDTO.class));
    }

    @Test
    void deveRetornarConflictQuandoDataPartidaAnteriorACriacaoDoClube() throws Exception {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        int golsMandante = 3;
        int golsVisitante = 2;
        LocalDateTime dataPartida = LocalDateTime.of(2025,3,11,14,15,16);

        CriarPartidaRequestDTO dto =
        new CriarPartidaRequestDTO(clubeMandanteId,clubeVisitanteId,estadioId,golsMandante,golsVisitante,dataPartida);

        Mockito.when(criarPartidaService.criarPartida(Mockito.any(CriarPartidaRequestDTO.class)))
                .thenThrow(new DataPartidaAnteriorACriacaoDoClubeException());

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value("DATA_PARTIDA_ANTERIOR_A_CRIACAO_DO_CLUBE"))
                .andExpect(jsonPath("$.mensagem").value("Não pode cadastrar uma partida para uma data anterior à data de criação do clube."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarPartidaService, Mockito.times(1))
                .criarPartida(Mockito.any(CriarPartidaRequestDTO.class));
    }

    @Test
    void deveRetornarConflictQuandoClubeInativo() throws Exception {
        Long clubeMandanteId = 4L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        int golsMandante = 3;
        int golsVisitante = 2;
        LocalDateTime dataPartida = LocalDateTime.of(2025,3,11,14,15,16);

        CriarPartidaRequestDTO dto =
                new CriarPartidaRequestDTO(clubeMandanteId,clubeVisitanteId,estadioId,golsMandante,golsVisitante,dataPartida);

        Mockito.when(criarPartidaService.criarPartida(Mockito.any(CriarPartidaRequestDTO.class)))
                .thenThrow(new ClubeInativoException());

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value("CLUBE_INATIVO"))
                .andExpect(jsonPath("$.mensagem").value("Não é possivel criar a partida pois há um clube inativo"))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarPartidaService, Mockito.times(1))
                .criarPartida(Mockito.any(CriarPartidaRequestDTO.class));
    }

    @Test
    void deveRetornarConflictQuandoEstadioJaPossuiJogoNoDia() throws Exception {
        Long clubeMandanteId = 1L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        int golsMandante = 3;
        int golsVisitante = 2;
        LocalDateTime dataPartida = LocalDateTime.of(2025,3,11,14,15,16);

        CriarPartidaRequestDTO dto =
                new CriarPartidaRequestDTO(clubeMandanteId,clubeVisitanteId,estadioId,golsMandante,golsVisitante,dataPartida);

        Mockito.when(criarPartidaService.criarPartida(Mockito.any(CriarPartidaRequestDTO.class)))
                .thenThrow(new EstadioJaPossuiPartidaNoMesmoDiaException());

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value("ESTADIO_JA_POSSUI_PARTIDA_MARCADA_NO_MESMO_DIA"))
                .andExpect(jsonPath("$.mensagem").value("Não é possivel criar a partida pois no estádio já tem uma partida marcada para o mesmo dia"))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarPartidaService, Mockito.times(1))
                .criarPartida(Mockito.any(CriarPartidaRequestDTO.class));
    }

    @Test
    void deveRetornarConflictQuandoClubeJaTiverPartidasDentroDe48HorasDaNovaPartida() throws Exception {
        Long clubeMandanteId = 4L;
        Long clubeVisitanteId = 2L;
        Long estadioId = 1L;
        int golsMandante = 3;
        int golsVisitante = 2;
        LocalDateTime dataPartida = LocalDateTime.of(2025,3,11,14,15,16);

        CriarPartidaRequestDTO dto =
                new CriarPartidaRequestDTO(clubeMandanteId,clubeVisitanteId,estadioId,golsMandante,golsVisitante,dataPartida);

        Mockito.when(criarPartidaService.criarPartida(Mockito.any(CriarPartidaRequestDTO.class)))
                .thenThrow(new ClubesComPartidasEmHorarioMenorQue48HorasException());

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value("CLUBE_TEM_PARTIDAS_COM_DATA_MENOR_QUE_48_HORAS_DA_NOVA_PARTIDA"))
                .andExpect(jsonPath("$.mensagem").value("Não é possível criar a partida pois um dos clubes já possui uma partida cadastrada em menos de 48 horas desta data."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarPartidaService, Mockito.times(1))
                .criarPartida(Mockito.any(CriarPartidaRequestDTO.class));
    }



}
