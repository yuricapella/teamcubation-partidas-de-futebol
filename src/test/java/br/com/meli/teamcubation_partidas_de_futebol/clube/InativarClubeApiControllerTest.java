package br.com.meli.teamcubation_partidas_de_futebol.clube;

import br.com.meli.teamcubation_partidas_de_futebol.clube.controller.InativarClubeApiController;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.InativarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.GlobalApiExceptionHandler;
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
public class InativarClubeApiControllerTest {
    @InjectMocks
    InativarClubeApiController inativarClubeApiController;
    @Mock
    InativarClubeService inativarClubeService;

    MockMvc mockMvc;

    final String PATH_COM_ID = "/api/clube/inativar/{id}";

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(inativarClubeApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler(), new ClubeApiExceptionHandler())
                .build();
    }

    @Test
    void deveInativarClubePorIdComSucessoERetornarNoContent() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(inativarClubeService).inativarClubePorId(id);

        mockMvc.perform(delete(PATH_COM_ID,id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(inativarClubeService, Mockito.times(1)).inativarClubePorId(id);
    }

    @Test
    void deveRetornarNotFoundAoInativarClubeInexistente() throws Exception {
        Long idInvalido = 999L;

        Mockito.doThrow(new ClubeNaoEncontradoException(idInvalido))
                .when(inativarClubeService).inativarClubePorId(idInvalido);

        mockMvc.perform(delete(PATH_COM_ID,idInvalido)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
            .andDo(MockMvcResultHandlers.print());

        Mockito.verify(inativarClubeService, Mockito.times(1)).inativarClubePorId(idInvalido);
    }


}
