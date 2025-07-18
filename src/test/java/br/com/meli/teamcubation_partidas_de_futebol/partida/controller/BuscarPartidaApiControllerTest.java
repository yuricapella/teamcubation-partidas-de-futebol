package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.global_exception.GlobalApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.PartidaApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.PartidaNaoEncontradaException;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.BuscarPartidaService;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaUtil;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class BuscarPartidaApiControllerTest {
    @InjectMocks
    BuscarPartidaApiController buscarPartidaApiController;
    @Mock
    BuscarPartidaService buscarPartidaService;

    MockMvc mockMvc;

    final String PATH = "/api/partida/buscar";
    final String PATH_COM_ID = "/api/partida/buscar/{id}";

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(buscarPartidaApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler(), new PartidaApiExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @ParameterizedTest
    @CsvSource({
            ",,,,,",                        // todos nulos
            "1,,,,,",                       // apenas clubeId
            ",2,,,,",                       // apenas estadioId
            ",,true,,,",                    // apenas goleada
            ",,,true,,",                    // apenas mandante
            ",,,,true,",                    // apenas visitante
            "1,2,true,true,false,"          // todos combinados
    })
    void deveBuscarPartidasComFiltros(Long clubeId, Long estadioId, Boolean goleada, Boolean mandante, Boolean visitante) throws Exception {
        Page<Partida> partidasRetornadas = PartidaUtil.getPagePartida();

        Mockito.when(buscarPartidaService.listarPartidasFiltradas(
                any(), any(), any(), any(), any(), any(Pageable.class)
        )).thenReturn(partidasRetornadas);

        var requestBuilder = get(PATH)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON);

        if (clubeId != null) requestBuilder = requestBuilder.param("clubeId", clubeId.toString());
        if (estadioId != null) requestBuilder = requestBuilder.param("estadioId", estadioId.toString());
        if (goleada != null) requestBuilder = requestBuilder.param("goleada", goleada.toString());
        if (mandante != null) requestBuilder = requestBuilder.param("mandante", mandante.toString());
        if (visitante != null) requestBuilder = requestBuilder.param("visitante", visitante.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(partidasRetornadas.getContent().size()))
                .andExpect(jsonPath("$.content[0].clubeMandante.nome").value("Mandante"))
                .andExpect(jsonPath("$.content[1].clubeVisitante.siglaEstado").value("RJ"))
                .andExpect(jsonPath("$.content[0].estadio.nome").value("Estádio Teste"))
                .andExpect(jsonPath("$.content[0].golsMandante").value(2))
                .andExpect(jsonPath("$.content[0].golsVisitante").value(1))
                .andExpect(jsonPath("$.content[1].golsMandante").value(3))
                .andExpect(jsonPath("$.content[1].golsVisitante").value(3))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarPartidaService, Mockito.times(1)).listarPartidasFiltradas(
                any(), any(), any(), any(), any(), any(Pageable.class)
        );
    }

    @Test
    void deveBuscarPartidaPorIdComSucesso() throws Exception {
        Long id = 1L;
        Partida partida = PartidaUtil.getPartida();
        partida.setId(id);
        PartidaResponseDTO partidaDTO = PartidaResponseMapper.toPartidaResponseDTO(partida);

        Mockito.when(buscarPartidaService.buscarPartidaPorId(id)).thenReturn(partida);

        mockMvc.perform(get(PATH_COM_ID,id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.clubeMandante.nome").value(partidaDTO.getClubeMandante().getNome()))
                .andExpect(jsonPath("$.clubeMandante.siglaEstado").value(partidaDTO.getClubeMandante().getSiglaEstado()))
                .andExpect(jsonPath("$.clubeVisitante.nome").value(partidaDTO.getClubeVisitante().getNome()))
                .andExpect(jsonPath("$.clubeVisitante.siglaEstado").value(partidaDTO.getClubeVisitante().getSiglaEstado()))
                .andExpect(jsonPath("$.estadio.nome").value(partidaDTO.getEstadio().getNome()))
                .andExpect(jsonPath("$.golsMandante").value(partidaDTO.getGolsMandante()))
                .andExpect(jsonPath("$.golsVisitante").value(partidaDTO.getGolsVisitante()))
                .andExpect(jsonPath("$.dataHora").value(partidaDTO.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))))
                .andExpect(jsonPath("$.resultado").value(partidaDTO.getResultado().toString()))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarPartidaService, Mockito.times(1)).buscarPartidaPorId(id);
    }

    @Test
    void deveRetornarNotFoundQuandoClubeNaoExistir() throws Exception {
        Long id = 999L;
        Mockito.when(buscarPartidaService.buscarPartidaPorId(id))
                .thenThrow(new PartidaNaoEncontradaException(id));

        mockMvc.perform(get(PATH_COM_ID,id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
            .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarPartidaService, Mockito.times(1)).buscarPartidaPorId(id);
    }


}
