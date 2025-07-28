package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.GlobalApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.PartidaUtil;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.mapper.RetrospectosAdversariosMapper;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoAdversario;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoConfronto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.service.BuscarRetrospectoService;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static br.com.meli.teamcubation_partidas_de_futebol.retrospecto.util.RetrospectoUtil.assertListaPartidas;
import static br.com.meli.teamcubation_partidas_de_futebol.retrospecto.util.RetrospectoUtil.assertRetrospectoClube;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BuscarRetrospectoApiControllerTest {
    @InjectMocks
    BuscarRetrospectoApiController buscarRetrospectoApiController;
    @Mock
    BuscarRetrospectoService buscarRetrospectoService;

    MockMvc mockMvc;

    final String PATH_RETROSPECTO_GERAL = "/api/clube/{id}/retrospecto";
    final String PATH_RETROSPECTO_CONTRA_ADVERSARIOS = "/api/clube/{id}/retrospectos-adversarios";
    final String PATH_RETROSPECTO_CONFRONTO = "/api/clube/{idClube}/confronto/{idAdversario}";

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(buscarRetrospectoApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "true,",
            ",true",
            "true,true"
    })
    void deveBuscarRetrospecto_comSucesso(Boolean mandante, Boolean visitante) throws Exception {
        Long id = 1L;
        List<Partida> partidas = PartidaUtil.criarListPartidasComTesteUtils(2);
        Clube clube = partidas.getFirst().getClubeMandante();

        Retrospecto retrospecto = new Retrospecto(clube,partidas);

        Mockito.when(buscarRetrospectoService.buscarRetrospectoClube(
                        Mockito.eq(id), Mockito.any(), Mockito.any()))
                .thenReturn(retrospecto);

        var requestBuilder = get(PATH_RETROSPECTO_GERAL,id)
                .contentType(MediaType.APPLICATION_JSON);

        if (mandante != null) requestBuilder = requestBuilder.param("mandante", mandante.toString());
        if (visitante != null) requestBuilder = requestBuilder.param("visitante", visitante.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clube.nome").value("Clube Exemplo 1"))
                .andExpect(jsonPath("$.clube.siglaEstado").value("AM"))
                .andExpect(jsonPath("$.clube.dataCriacao[0]").value(2025))
                .andExpect(jsonPath("$.clube.dataCriacao[1]").value(11))
                .andExpect(jsonPath("$.clube.dataCriacao[2]").value(3))
                .andExpect(jsonPath("$.jogos").value(2))
                .andExpect(jsonPath("$.vitorias").value(2))
                .andExpect(jsonPath("$.derrotas").value(0))
                .andExpect(jsonPath("$.empates").value(0))
                .andExpect(jsonPath("$.golsFeitos").value(7)) // Exemplo: 3 + 4
                .andExpect(jsonPath("$.golsSofridos").value(5)) // Exemplo: 2 + 3
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarRetrospectoService, Mockito.times(1))
                .buscarRetrospectoClube(Mockito.eq(id), Mockito.eq(mandante), Mockito.eq(visitante));
    }

    @Test
    void deveRetornarRetrospectoZerado_quandoNaoHaPartidas() throws Exception {
        Long id = 10L;
        Clube clube = ClubeUtil.criarClube(id);
        Retrospecto retrospecto = new Retrospecto(clube, List.of());

        Mockito.when(buscarRetrospectoService.buscarRetrospectoClube(
                        Mockito.eq(id), Mockito.any(), Mockito.any()))
                .thenReturn(retrospecto);

        mockMvc.perform(get(PATH_RETROSPECTO_GERAL, id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clube.nome").value("Clube Exemplo 10"))
                .andExpect(jsonPath("$.clube.siglaEstado").value("AM"))
                .andExpect(jsonPath("$.clube.dataCriacao[0]").value(2025))
                .andExpect(jsonPath("$.clube.dataCriacao[1]").value(11))
                .andExpect(jsonPath("$.clube.dataCriacao[2]").value(3))
                .andExpect(jsonPath("$.jogos").value(0))
                .andExpect(jsonPath("$.vitorias").value(0))
                .andExpect(jsonPath("$.derrotas").value(0))
                .andExpect(jsonPath("$.empates").value(0))
                .andExpect(jsonPath("$.golsFeitos").value(0))
                .andExpect(jsonPath("$.golsSofridos").value(0))
                .andDo(MockMvcResultHandlers.print());

                Mockito.verify(buscarRetrospectoService, Mockito.times(1))
                .buscarRetrospectoClube(Mockito.eq(id), Mockito.any(), Mockito.any());
    }

    @Test
    void deveRetornarNotFound_quandoClubeNaoExiste_aoBuscarRetrospecto() throws Exception {
        Long id = 99L;

        Mockito.when(buscarRetrospectoService.buscarRetrospectoClube(
                        Mockito.eq(id), Mockito.any(), Mockito.any()))
                .thenThrow(new ClubeNaoEncontradoException(id));

        mockMvc.perform(get(PATH_RETROSPECTO_GERAL, id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Clube com id 99 não encontrado."))
                .andExpect(jsonPath("$.codigoErro").value("CLUBE_NAO_ENCONTRADO"))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarRetrospectoService, Mockito.times(1))
                .buscarRetrospectoClube(Mockito.eq(id), Mockito.any(), Mockito.any());
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "true,",
            ",true",
            "true,true"
    })
    void deveBuscarRetrospectoContraAdversario_comSucesso(Boolean mandante, Boolean visitante) throws Exception {
        Long id = 1L;
        List<Partida> partidas = PartidaUtil.criarListPartidasComTesteUtils(2);
        Clube clube = partidas.getFirst().getClubeMandante();
        Clube clubeAdversario = partidas.getFirst().getClubeVisitante();

        RetrospectoAdversario retrospectoAdversario = new RetrospectoAdversario(id,clubeAdversario,partidas);
        List<RetrospectoAdversario> retrospectosContraAdversario = new ArrayList<>();
        retrospectosContraAdversario.add(retrospectoAdversario);

        RetrospectoAdversariosResponseDTO resultado = RetrospectosAdversariosMapper.toDTO(clube.getNome(), clube.getSiglaEstado(), retrospectosContraAdversario);

        Mockito.when(buscarRetrospectoService.buscarRetrospectoClubeContraAdversarios(
                        Mockito.eq(id), Mockito.any(), Mockito.any()))
                .thenReturn(resultado);

        var requestBuilder = get(PATH_RETROSPECTO_CONTRA_ADVERSARIOS,id)
                .contentType(MediaType.APPLICATION_JSON);

        if (mandante != null) requestBuilder = requestBuilder.param("mandante", mandante.toString());
        if (visitante != null) requestBuilder = requestBuilder.param("visitante", visitante.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeClube").value("Clube Exemplo 1"))
                .andExpect(jsonPath("$.estadoClube").value("AM"))
                .andExpect(jsonPath("$.retrospectoContraAdversarios[0].nomeAdversario").value("Clube Exemplo 2"))
                .andExpect(jsonPath("$.retrospectoContraAdversarios[0].estadoAdversario").value("AM"))
                .andExpect(jsonPath("$.retrospectoContraAdversarios[0].jogos").value(2))
                .andExpect(jsonPath("$.retrospectoContraAdversarios[0].vitorias").value(2))
                .andExpect(jsonPath("$.retrospectoContraAdversarios[0].derrotas").value(0))
                .andExpect(jsonPath("$.retrospectoContraAdversarios[0].empates").value(0))
                .andExpect(jsonPath("$.retrospectoContraAdversarios[0].golsFeitos").value(7))
                .andExpect(jsonPath("$.retrospectoContraAdversarios[0].golsSofridos").value(5))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarRetrospectoService, Mockito.times(1))
                .buscarRetrospectoClubeContraAdversarios(Mockito.eq(id), Mockito.eq(mandante), Mockito.eq(visitante));
    }

    @Test
    void deveRetornarListaVaziaDeRetrospectoContraAdversario_quandoNaoTemPartidas() throws Exception {
        Long id = 14L;
        Clube clube = ClubeUtil.criarClube(id);

        RetrospectoAdversariosResponseDTO resultado = RetrospectosAdversariosMapper.toDTO(clube.getNome(), clube.getSiglaEstado(), List.of());

        Mockito.when(buscarRetrospectoService.buscarRetrospectoClubeContraAdversarios(
                        Mockito.eq(id), Mockito.any(), Mockito.any()))
                .thenReturn(resultado);

        var requestBuilder = get(PATH_RETROSPECTO_CONTRA_ADVERSARIOS,id)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeClube").value("Clube Exemplo 14"))
                .andExpect(jsonPath("$.estadoClube").value("AM"))
                .andExpect(jsonPath("$.retrospectoContraAdversarios").isEmpty())

                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarRetrospectoService, Mockito.times(1))
                .buscarRetrospectoClubeContraAdversarios(Mockito.eq(id), Mockito.any(), Mockito.any());
    }

    @Test
    void deveRetornarNotFound_quandoClubeNaoExiste_aoBuscarRetrospectoContraAdversario() throws Exception {
        Long id = 99L;

        Mockito.when(buscarRetrospectoService.buscarRetrospectoClubeContraAdversarios(
                        Mockito.eq(id), Mockito.any(), Mockito.any()))
                .thenThrow(new ClubeNaoEncontradoException(id));

        mockMvc.perform(get(PATH_RETROSPECTO_CONTRA_ADVERSARIOS, id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Clube com id 99 não encontrado."))
                .andExpect(jsonPath("$.codigoErro").value("CLUBE_NAO_ENCONTRADO"))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarRetrospectoService, Mockito.times(1))
                .buscarRetrospectoClubeContraAdversarios(Mockito.eq(id), Mockito.any(), Mockito.any());
    }

    @Test
    void deveBuscarRetrospectoConfronto_comSucesso() throws Exception {
        Long idClube = 1L;
        Long idAdversario = 2L;
        Clube clube = ClubeUtil.criarClube(idClube);
        Clube clubeAdversario = ClubeUtil.criarClube(idAdversario);
        List<Partida> partidas = PartidaUtil.criarListPartidasComTesteUtils(2);

        Retrospecto retrospectoClube = new Retrospecto(clube,partidas);
        Retrospecto retrospectoAdversario = new Retrospecto(clubeAdversario,partidas);

        List<Retrospecto> retrospectos = List.of(retrospectoClube, retrospectoAdversario);
        List<PartidaResponseDTO> partidasDTO = PartidaResponseMapper.toPartidaResponseDTO(partidas);

        RetrospectoConfronto resultado = new RetrospectoConfronto(retrospectos,partidasDTO);

        Mockito.when(buscarRetrospectoService.buscarRetrospectoConfronto(
                idClube,idAdversario
        )).thenReturn(resultado);

        var requestBuilder = get(PATH_RETROSPECTO_CONFRONTO,idClube,idAdversario)
                .contentType(MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertRetrospectoClube(result, 0, "Clube Exemplo 1", "AM", 2025, 11, 3,
                2, 2, 0, 0, 7, 5);

        assertRetrospectoClube(result, 1, "Clube Exemplo 2", "AM", 2025, 11, 3,
                2, 0, 2, 0, 5, 7);

        assertListaPartidas(result, 0,
                "Clube Exemplo 1", "AM", 2025, 11, 3,
                "Clube Exemplo 2", "AM", 2025, 11, 3,
                "Estadio Exemplo",
                3, 2,
                "10/07/2023 16:00:00",
                "VITORIA_MANDANTE"
        );

        assertListaPartidas(result, 1,
                "Clube Exemplo 1", "AM", 2025, 11, 3,
                "Clube Exemplo 2", "AM", 2025, 11, 3,
                "Estadio Exemplo",
                4, 3,
                "11/07/2023 16:00:00",
                "VITORIA_MANDANTE"
        );

        Mockito.verify(buscarRetrospectoService, Mockito.times(1))
                .buscarRetrospectoConfronto(idClube,idAdversario);
    }

    @Test
    void deveRetornarRetrospectoConfronto_comRetrospectosZerados_eListaPartidasVazia() throws Exception {
        Long idClube = 1L;
        Long idAdversario = 4L;
        Clube clube = ClubeUtil.criarClube(idClube);
        Clube clubeAdversario = ClubeUtil.criarClube(idAdversario);
        List<Partida> partidas = List.of();

        Retrospecto retrospectoClube = new Retrospecto(clube,partidas);
        Retrospecto retrospectoAdversario = new Retrospecto(clubeAdversario,partidas);

        List<Retrospecto> retrospectos = List.of(retrospectoClube, retrospectoAdversario);
        List<PartidaResponseDTO> partidasDTO = PartidaResponseMapper.toPartidaResponseDTO(partidas);

        RetrospectoConfronto resultado = new RetrospectoConfronto(retrospectos,partidasDTO);

        Mockito.when(buscarRetrospectoService.buscarRetrospectoConfronto(
                idClube,idAdversario
        )).thenReturn(resultado);

        var requestBuilder = get(PATH_RETROSPECTO_CONFRONTO,idClube,idAdversario)
                .contentType(MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partidas").isEmpty())
                .andDo(MockMvcResultHandlers.print());

        assertRetrospectoClube(result, 0, "Clube Exemplo 1", "AM", 2025, 11, 3,
                0, 0, 0, 0, 0, 0);

        assertRetrospectoClube(result, 1, "Clube Exemplo 4", "AM", 2025, 11, 3,
                0, 0, 0, 0, 0, 0);

        Mockito.verify(buscarRetrospectoService, Mockito.times(1))
                .buscarRetrospectoConfronto(idClube,idAdversario);
    }

    @Test
    void deveRetornarNotFound_quandoClubeNaoExiste_aoBuscarRetrospectoConfronto() throws Exception {
        Long clubeid = 999L;
        Long idAdversario = 4L;

        Mockito.when(buscarRetrospectoService.buscarRetrospectoConfronto(
                        clubeid,idAdversario))
                .thenThrow(new ClubeNaoEncontradoException(clubeid));

        mockMvc.perform(get(PATH_RETROSPECTO_CONFRONTO, clubeid, idAdversario)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Clube com id 999 não encontrado."))
                .andExpect(jsonPath("$.codigoErro").value("CLUBE_NAO_ENCONTRADO"))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarRetrospectoService, Mockito.times(1))
                .buscarRetrospectoConfronto(clubeid,idAdversario);
    }
}
