package br.com.meli.teamcubation_partidas_de_futebol.partida.controller;

import br.com.meli.teamcubation_partidas_de_futebol.global_exception.GlobalApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.PartidaApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.partida.exception.PartidaNaoEncontradaException;
import br.com.meli.teamcubation_partidas_de_futebol.partida.service.DeletarPartidaService;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class DeletarPartidaApiControllerTest {
    @InjectMocks
    DeletarPartidaApiController deletarPartidaApiController;
    @Mock
    DeletarPartidaService deletarPartidaService;

    MockMvc mockMvc;

    final String PATH_COM_ID = "/api/partida/deletar/{id}";

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(deletarPartidaApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler(), new PartidaApiExceptionHandler())
                .build();
    }

    @Test
    void deveDeletarPartidaPorIdComSucessoERetornarNoContent() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(deletarPartidaService).deletarPartidaPorId(id);

        mockMvc.perform(delete(PATH_COM_ID,id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(deletarPartidaService, Mockito.times(1)).deletarPartidaPorId(id);
    }

    @Test
    void deveRetornarNotFoundAoDeletarPartidaInexistente() throws Exception {
        Long idInvalido = 999L;

        Mockito.doThrow(new PartidaNaoEncontradaException(idInvalido))
                .when(deletarPartidaService).deletarPartidaPorId(idInvalido);

        mockMvc.perform(delete(PATH_COM_ID,idInvalido)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
            .andDo(MockMvcResultHandlers.print());

        Mockito.verify(deletarPartidaService, Mockito.times(1)).deletarPartidaPorId(idInvalido);
    }


}
