package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.CriarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeComNomeJaCadastradoNoEstadoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.EstadoInexistenteException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.CriarClubeService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class CriarClubeApiControllerTest {
    @InjectMocks
    CriarClubeApiController criarClubeApiController;
    @Mock
    CriarClubeService criarClubeService;

    MockMvc mockMvc;

    final String PATH = "/api/clube/criar";

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(criarClubeApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler(), new ClubeApiExceptionHandler())
                .build();
    }

    @Test
    void deveCriarClubeComSucessoERetornar201Created() throws Exception {
        Long id = 1L;
        CriarClubeRequestDTO criarDTO = new CriarClubeRequestDTO(
                "Clube de time criado", "AM", LocalDate.of(2020, 3, 11)
        );
        Clube clubeCriado = new Clube();
        clubeCriado.setId(id);
        clubeCriado.setNome(criarDTO.getNome());
        clubeCriado.setSiglaEstado(criarDTO.getSiglaEstado());
        clubeCriado.setDataCriacao(criarDTO.getDataCriacao());

        Mockito.when(criarClubeService.criarClube(Mockito.any(CriarClubeRequestDTO.class)))
                .thenReturn(clubeCriado);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(criarDTO)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarClubeService, Mockito.times(1))
                .criarClube(Mockito.argThat(dto ->
                        dto.getNome().equals("Clube de time criado") &&
                                dto.getSiglaEstado().equals("AM") &&
                                dto.getDataCriacao().equals(LocalDate.of(2020, 3, 11))
                ));
    }

    @ParameterizedTest
    @CsvSource({
            "A,AM,2023-01-01,nome,O nome tem que ter no minimo duas letras;",
            "123,AM,2023-01-01,nome,O nome deve conter apenas letras e espaços",
            "Clube,S,2023-01-01,siglaEstado,A sigla do estado só pode ter 2 letras.",
            "Clube,AM,,dataCriacao,Data de criação obrigatória",
            "Clube,AM,2099-01-01,dataCriacao,A data de criação não pode ser futura"
    })
    void deveRetornarBadRequestQuandoCriarClubeComDtoInvalido(
            String nome, String siglaEstado, String dataCriacaoStr,
            String campoErro, String mensagemEsperada
    ) throws Exception {
        Long id = 1L;
        LocalDate dataCriacao = (dataCriacaoStr == null || dataCriacaoStr.isBlank()) ? null : LocalDate.parse(dataCriacaoStr);

        CriarClubeRequestDTO dtoInvalido = new CriarClubeRequestDTO(nome, siglaEstado, dataCriacao);

        mockMvc.perform(post(PATH)
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
        CriarClubeRequestDTO dtoValido = new CriarClubeRequestDTO(
                "Clube Teste", siglaInvalida, LocalDate.of(2020, 1, 1)
        );

        Mockito.when(criarClubeService.criarClube(Mockito.any(CriarClubeRequestDTO.class)))
                .thenThrow(new EstadoInexistenteException(siglaInvalida));

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dtoValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigoErro").value("ESTADO_INEXISTENTE"))
                .andExpect(jsonPath("$.mensagem").value("Não é possivel criar o clube pois o estado XX não existe."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarClubeService, Mockito.times(1))
                .criarClube(Mockito.any(CriarClubeRequestDTO.class));
    }

    @Test
    void deveRetornarConflictQuandoNomeJaCadastradoNoEstado() throws Exception {
        Long idInvalido = 999L;
        CriarClubeRequestDTO dtoValido = new CriarClubeRequestDTO(
                "Clube Teste", "SP", LocalDate.of(2020, 1, 1)
        );

        Mockito.when(criarClubeService.criarClube(Mockito.any(CriarClubeRequestDTO.class)))
                .thenThrow(new ClubeComNomeJaCadastradoNoEstadoException());

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dtoValido)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value("CLUBE_DUPLICADO"))
                .andExpect(jsonPath("$.mensagem").value("Já existe clube com este nome no mesmo estado."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarClubeService, Mockito.times(1))
                .criarClube(Mockito.any(CriarClubeRequestDTO.class));
    }

}



