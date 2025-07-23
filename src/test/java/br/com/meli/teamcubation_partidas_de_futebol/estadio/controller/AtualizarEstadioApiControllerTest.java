package br.com.meli.teamcubation_partidas_de_futebol.estadio.controller;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.AtualizarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioJaExisteException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.AtualizarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.GlobalApiExceptionHandler;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class AtualizarEstadioApiControllerTest {
    @InjectMocks
    AtualizarEstadioApiController atualizarEstadioApiController;
    @Mock
    AtualizarEstadioService atualizarEstadioService;

    MockMvc mockMvc;

    final String PATH_COM_ID = "/api/estadio/atualizar/{id}";
    AtualizarEstadioRequestDTO atualizarDTO = new AtualizarEstadioRequestDTO("Estadio de time atualizado");

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(atualizarEstadioApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler(), new EstadioApiExceptionHandler())
                .build();
    }

    @Test
    void deveAtualizarEstadioComSucessoERetornar200OK() throws Exception {
        Long id = 1L;
        Estadio estadioAtualizado = new Estadio();
        estadioAtualizado.setId(id);
        estadioAtualizado.setNome(atualizarDTO.getNome());

        Mockito.when(atualizarEstadioService.atualizarEstadio(Mockito.any(AtualizarEstadioRequestDTO.class),
                        Mockito.eq(id))).thenReturn(estadioAtualizado);

        mockMvc.perform(put(PATH_COM_ID,id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(atualizarDTO)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(atualizarEstadioService, Mockito.times(1))
                .atualizarEstadio(Mockito.argThat(dto ->
                        dto.getNome().equals("Estadio de time atualizado")), Mockito.eq(id));
    }

    @ParameterizedTest
    @CsvSource({
            "A,nome,O nome tem que ter no minimo três letras;",
            "12345!,nome,O nome deve conter apenas letras sem acento e espaços"
    })
    void deveRetornarBadRequestQuandoAtualizarEstadioComDtoInvalido(
            String nome,
            String campoErro, String mensagemEsperada
    ) throws Exception {
        Long id = 1L;

        AtualizarEstadioRequestDTO dtoInvalido = new AtualizarEstadioRequestDTO(nome);

        mockMvc.perform(put(PATH_COM_ID,id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigoErro").value("CAMPO_INVALIDO"))
                .andExpect(jsonPath("$.errors." + campoErro).value(mensagemEsperada))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void deveRetornarConflictQuandoNomeJaCadastrado() throws Exception {
        Long id = 1L;
        Mockito.when(atualizarEstadioService.atualizarEstadio
                (Mockito.any(AtualizarEstadioRequestDTO.class),Mockito.eq(id)))
                .thenThrow(new EstadioJaExisteException());

        mockMvc.perform(put(PATH_COM_ID,id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(atualizarDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value("ESTADIO_JA_EXISTE"))
                .andExpect(jsonPath("$.mensagem").value("Já existe um estadio com este nome."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(atualizarEstadioService, Mockito.times(1))
                .atualizarEstadio(Mockito.any(AtualizarEstadioRequestDTO.class),Mockito.eq(id));
    }

    @Test
    void deveRetornarNotFoundQuandoAtualizarEstadioComIdInexistente() throws Exception {
        Long idInvalido = 999L;

        Mockito.when(atualizarEstadioService.atualizarEstadio
                (Mockito.any(AtualizarEstadioRequestDTO.class), Mockito.eq(idInvalido)))
                .thenThrow(new EstadioNaoEncontradoException(idInvalido));

        mockMvc.perform(put(PATH_COM_ID, idInvalido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(atualizarDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigoErro").value("ESTADIO_NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.mensagem").value("Estadio com id 999 não encontrado."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(atualizarEstadioService, Mockito.times(1))
                .atualizarEstadio(Mockito.any(AtualizarEstadioRequestDTO.class), Mockito.eq(idInvalido));
    }

}



