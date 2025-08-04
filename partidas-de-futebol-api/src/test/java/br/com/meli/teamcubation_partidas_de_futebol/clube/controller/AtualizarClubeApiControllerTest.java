package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.AtualizarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.*;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.AtualizarClubeService;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class AtualizarClubeApiControllerTest {
    @InjectMocks
    AtualizarClubeApiController atualizarClubeApiController;
    @Mock
    AtualizarClubeService atualizarClubeService;

    MockMvc mockMvc;

    final String PATH_COM_ID = "/api/clube/atualizar/{id}";

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(atualizarClubeApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler(), new ClubeApiExceptionHandler())
                .build();
    }

    @Test
    void deveAtualizarClubePorIdComSucesso() throws Exception {
        Long id = 1L;
        AtualizarClubeRequestDTO atualizarDTO = new AtualizarClubeRequestDTO(
                "Clube de time atualizado", "AM", LocalDate.of(2020, 3, 11)
        );
        Clube clubeAtualizado = new Clube();
        clubeAtualizado.setId(id);
        clubeAtualizado.setNome(atualizarDTO.getNome());
        clubeAtualizado.setSiglaEstado(atualizarDTO.getSiglaEstado());
        clubeAtualizado.setDataCriacao(atualizarDTO.getDataCriacao());

        Mockito.when(atualizarClubeService.atualizar(Mockito.any(AtualizarClubeRequestDTO.class), Mockito.eq(id)))
                .thenReturn(clubeAtualizado);

        mockMvc.perform(put(PATH_COM_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(atualizarDTO)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(atualizarClubeService, Mockito.times(1))
                .atualizar(Mockito.argThat(dto ->
                        dto.getNome().equals("Clube de time atualizado") &&
                                dto.getSiglaEstado().equals("AM") &&
                                dto.getDataCriacao().equals(LocalDate.of(2020, 3, 11))
                ), Mockito.eq(id));
    }

    @Test
    void deveRetornarNotFoundQuandoAtualizarClubeComIdInexistente() throws Exception {
        Long idInvalido = 999L;

        AtualizarClubeRequestDTO atualizarDTO = new AtualizarClubeRequestDTO(
                "Clube de time atualizado", "AM", LocalDate.of(2020, 3, 11)
        );

        Mockito.when(atualizarClubeService.atualizar(Mockito.any(), Mockito.eq(idInvalido)))
                .thenThrow(new ClubeNaoEncontradoException(idInvalido));

        mockMvc.perform(put(PATH_COM_ID, idInvalido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(atualizarDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigoErro").value("CLUBE_NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.mensagem").value("Clube com id 999 não encontrado."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(atualizarClubeService, Mockito.times(1))
                .atualizar(Mockito.any(AtualizarClubeRequestDTO.class), Mockito.eq(idInvalido));
    }

    @ParameterizedTest
    @CsvSource({
            "A,AM,2023-01-01,nome,O nome tem que ter no minimo duas letras;",
            "123,AM,2023-01-01,nome,O nome deve conter apenas letras e espaços",
            "Clube,S,2023-01-01,siglaEstado,A sigla do estado só pode ter 2 letras.",
            "Clube,AM,,dataCriacao,Data de criação obrigatória",
            "Clube,AM,2099-01-01,dataCriacao,A data de criação não pode ser futura"
    })
    void deveRetornarBadRequestQuandoAtualizarClubeComDtoInvalido(
            String nome, String siglaEstado, String dataCriacaoStr,
            String campoErro, String mensagemEsperada
    ) throws Exception {
        Long id = 1L;
        LocalDate dataCriacao = (dataCriacaoStr == null || dataCriacaoStr.isBlank()) ? null : LocalDate.parse(dataCriacaoStr);

        AtualizarClubeRequestDTO dtoInvalido = new AtualizarClubeRequestDTO(nome, siglaEstado, dataCriacao);

        mockMvc.perform(put(PATH_COM_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigoErro").value("CAMPO_INVALIDO"))
                .andExpect(jsonPath("$.errors." + campoErro).value(mensagemEsperada))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void deveRetornarBadRequestQuandoEstadoInexistente() throws Exception {
        Long id = 321L;
        String siglaInvalida = "XX";
        AtualizarClubeRequestDTO dtoValido = new AtualizarClubeRequestDTO(
                "Clube Teste", siglaInvalida, LocalDate.of(2020, 1, 1)
        );

        Mockito.when(atualizarClubeService.atualizar(Mockito.any(), Mockito.eq(id)))
                .thenThrow(new EstadoInexistenteException(siglaInvalida));

        mockMvc.perform(put(PATH_COM_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dtoValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigoErro").value("ESTADO_INEXISTENTE"))
                .andExpect(jsonPath("$.mensagem").value("Não é possivel criar o clube pois o estado XX não existe."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(atualizarClubeService, Mockito.times(1))
                .atualizar(Mockito.any(AtualizarClubeRequestDTO.class), Mockito.eq(id));
    }

    @Test
    void deveRetornarConflictQuandoNomeJaCadastradoNoEstado() throws Exception {
        Long idInvalido = 999L;
        AtualizarClubeRequestDTO dtoValido = new AtualizarClubeRequestDTO(
                "Clube Teste", "SP", LocalDate.of(2020, 1, 1)
        );

        Mockito.when(atualizarClubeService.atualizar(Mockito.any(), Mockito.eq(idInvalido)))
                .thenThrow(new ClubeComNomeJaCadastradoNoEstadoException());

        mockMvc.perform(put(PATH_COM_ID, idInvalido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dtoValido)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value("CLUBE_DUPLICADO"))
                .andExpect(jsonPath("$.mensagem").value("Já existe clube com este nome no mesmo estado."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(atualizarClubeService, Mockito.times(1))
                .atualizar(Mockito.any(AtualizarClubeRequestDTO.class), Mockito.eq(idInvalido));
    }

    @Test
    void deveRetornarConflictQuandoDataCriacaoPosteriorAPartida() throws Exception {
        Long id = 123L;
        AtualizarClubeRequestDTO dtoValido = new AtualizarClubeRequestDTO(
                "Clube Teste", "SP", LocalDate.of(2025, 1, 1)
        );

        Mockito.when(atualizarClubeService.atualizar(Mockito.any(), Mockito.eq(id)))
                .thenThrow(new DataCriacaoPosteriorDataPartidaException(id));

        mockMvc.perform(put(PATH_COM_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dtoValido)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value("DATA_CRIACAO_POSTERIOR_A_DATA_PARTIDA"))
                .andExpect(jsonPath("$.mensagem").value("A data de criação do clube com id 123 está posterior a data de uma partida cadastrada."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(atualizarClubeService, Mockito.times(1))
                .atualizar(Mockito.any(AtualizarClubeRequestDTO.class), Mockito.eq(id));
    }


}



