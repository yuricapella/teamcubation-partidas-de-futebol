package br.com.meli.teamcubation_partidas_de_futebol.ranking.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.GlobalApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.model.Ranking;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.service.RankingService;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.util.RankingUtil;
import br.com.meli.teamcubation_partidas_de_futebol.ranking.util.TipoRanking;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.util.RetrospectoUtil;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RankingApiControllerTest {
    @InjectMocks
    RankingApiController rankingApiController;
    @Mock
    RankingService rankingService;

    MockMvc mockMvc;

    final String PATH = "/api/clube/ranking";

    @BeforeEach
    public void setup(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(rankingApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @ParameterizedTest
    @EnumSource(TipoRanking.class)
    void deveBuscarRankingPorClub(TipoRanking tipoRanking) throws Exception {
        Long clubeId = 1L;
        Long clubeId2 = 2L;
        Clube clube = ClubeUtil.criarClube(clubeId);
        Clube clube2 = ClubeUtil.criarClube(clubeId2);
        Retrospecto retrospecto = RetrospectoUtil.criaRetrospecto(clube);
        Retrospecto retrospecto2 = RetrospectoUtil.criaRetrospecto(clube2);

        var mock = RankingUtil.criarMockRanking(tipoRanking, clube, clube2, retrospecto, retrospecto2);

        Mockito.<List<? extends Ranking>>when(rankingService.calcularRanking(tipoRanking.name()))
                .thenReturn(mock.lista());

        var requestBuilder = get(PATH)
                .param("tipoRanking", tipoRanking.name())
                .contentType(MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        if (mock.totalClube1() > 0) {
            result.andExpect(jsonPath("$[0].nomeClube").value(clube.getNome()))
                    .andExpect(jsonPath("$[0].estadoClube").value(clube.getSiglaEstado()))
                    .andExpect(jsonPath("$[0].total").value(mock.totalClube1()));
        }

        if (mock.totalClube2() > 0) {
            int index = mock.totalClube1() > 0 ? 1 : 0;
            result.andExpect(jsonPath("$[" + index + "].nomeClube").value(clube2.getNome()))
                    .andExpect(jsonPath("$[" + index + "].estadoClube").value(clube2.getSiglaEstado()))
                    .andExpect(jsonPath("$[" + index + "].total").value(mock.totalClube2()));
        }

        result.andDo(MockMvcResultHandlers.print());
    }

    @ParameterizedTest
    @EnumSource(TipoRanking.class)
    void deveRetornarListaVazia_quandoNaoExistiremClubes(TipoRanking tipoRanking) throws Exception {
        Mockito.<List<? extends Ranking>>when(rankingService.calcularRanking(tipoRanking.name()))
                .thenReturn(List.of());

        var requestBuilder = get(PATH)
                .param("tipoRanking", tipoRanking.name())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deveLancarExcecao_quandoTipoRankingInvalido() throws Exception {
        String tipoInvalido = "TIPO_NAO_EXISTENTE";

        Mockito.when(rankingService.calcularRanking(tipoInvalido))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "tipoRanking inválido! Use valores: TOTAL_PONTOS, TOTAL_GOLS, TOTAL_VITORIAS, TOTAL_JOGOS."));

        var requestBuilder = get(PATH)
                .param("tipoRanking", tipoInvalido)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("tipoRanking inválido! Use valores: TOTAL_PONTOS, TOTAL_GOLS, TOTAL_VITORIAS, TOTAL_JOGOS."))
                .andExpect(jsonPath("$.codigoErro").value("400 BAD_REQUEST"))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(rankingService, Mockito.times(1)).calcularRanking(tipoInvalido);
    }
}

