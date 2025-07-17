package br.com.meli.teamcubation_partidas_de_futebol.estadio;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.controller.CriarEstadioApiController;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CriarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioJaExisteException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.CriarEstadioService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class CriarEstadioApiControllerTest {
    @InjectMocks
    CriarEstadioApiController criarEstadioApiController;
    @Mock
    CriarEstadioService criarEstadioService;

    MockMvc mockMvc;

    final String PATH = "/api/estadio/criar";

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(criarEstadioApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler(), new EstadioApiExceptionHandler())
                .build();
    }

    @Test
    void deveCriarEstadioComSucessoERetornar201Created() throws Exception {
        Long id = 1L;
        CriarEstadioRequestDTO criarDTO = new CriarEstadioRequestDTO("Estadio de time criado");
        Estadio estadioCriado = new Estadio();
        estadioCriado.setId(id);
        estadioCriado.setNome(criarDTO.getNome());

        Mockito.when(criarEstadioService.criarEstadio(Mockito.any(CriarEstadioRequestDTO.class)))
                .thenReturn(estadioCriado);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(criarDTO)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarEstadioService, Mockito.times(1))
                .criarEstadio(Mockito.argThat(dto ->
                        dto.getNome().equals("Estadio de time criado")));
    }

    @ParameterizedTest
    @CsvSource({
            "A,nome,O nome tem que ter no minimo três letras;",
            "12345!,nome,O nome deve conter apenas letras sem acento e espaços"
    })
    void deveRetornarBadRequestQuandoCriarEstadioComDtoInvalido(
            String nome,
            String campoErro, String mensagemEsperada
    ) throws Exception {
        Long id = 1L;

        CriarEstadioRequestDTO dtoInvalido = new CriarEstadioRequestDTO(nome);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigoErro").value("CAMPO_INVALIDO"))
                .andExpect(jsonPath("$.errors." + campoErro).value(mensagemEsperada))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void deveRetornarConflictQuandoNomeJaCadastrado() throws Exception {
        CriarEstadioRequestDTO criarDTO = new CriarEstadioRequestDTO("Estadio de time criado");

        Mockito.when(criarEstadioService.criarEstadio(Mockito.any(CriarEstadioRequestDTO.class)))
                .thenThrow(new EstadioJaExisteException());

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(criarDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value("ESTADIO_JA_EXISTE"))
                .andExpect(jsonPath("$.mensagem").value("Já existe um estadio com este nome."))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(criarEstadioService, Mockito.times(1))
                .criarEstadio(Mockito.any(CriarEstadioRequestDTO.class));
    }

}



