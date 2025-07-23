package br.com.meli.teamcubation_partidas_de_futebol.estadio.controller;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper.EstadioResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.service.BuscarEstadioService;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.GlobalApiExceptionHandler;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class BuscarEstadioApiControllerTest {
    @InjectMocks
    BuscarEstadioApiController buscarEstadioApiController;
    @Mock
    BuscarEstadioService buscarEstadioService;

    MockMvc mockMvc;

    final String PATH = "/api/estadio/buscar";
    final String PATH_COM_ID = "/api/estadio/buscar/{id}";

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(buscarEstadioApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler(), new EstadioApiExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @ParameterizedTest
    @CsvSource({
            "''",
            "estadio de time"
    })
    void deveBuscarTodosEstadiosComSucesso(String nome) throws Exception {
        EstadioResponseDTO dto1 = new EstadioResponseDTO("estadio de time");
        EstadioResponseDTO dto2 = new EstadioResponseDTO("outro estadio");

        Page<EstadioResponseDTO> clubesRetornados = new PageImpl<>(new ArrayList<>(List.of(dto1, dto2)),
                org.springframework.data.domain.PageRequest.of(0, 10),
                2L);


        Mockito.when(buscarEstadioService.listarEstadiosFiltrados(
                any(), any(Pageable.class)
        )).thenReturn(clubesRetornados);

        var requestBuilder = get(PATH)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON);

        if (nome != null && !nome.isEmpty()) requestBuilder = requestBuilder.param("nome", nome);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(clubesRetornados.getContent().size()))
                .andExpect(jsonPath("$.content[0].nome").value("estadio de time"))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarEstadioService, Mockito.times(1)).listarEstadiosFiltrados(
                any(), any(Pageable.class)
        );
    }

    @Test
    void deveBuscarEstadioPorIdComSucesso() throws Exception {
        Long id = 1L;
        Estadio estadio = new Estadio("estadio de time");
        estadio.setId(id);
        EstadioResponseDTO estadioDTO = EstadioResponseMapper.toEstadioResponseDTO(estadio);

        Mockito.when(buscarEstadioService.buscarEstadioPorId(id)).thenReturn(estadio);

        mockMvc.perform(get(PATH_COM_ID,id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.nome").value(estadioDTO.getNome()))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarEstadioService, Mockito.times(1)).buscarEstadioPorId(id);
    }

    @Test
    void deveRetornarNotFoundQuandoEstadioNaoExistir() throws Exception {
        Long id = 999L;
        Mockito.when(buscarEstadioService.buscarEstadioPorId(id))
                .thenThrow(new EstadioNaoEncontradoException(id));

        mockMvc.perform(get(PATH_COM_ID,id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
            .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarEstadioService, Mockito.times(1)).buscarEstadioPorId(id);
    }


}
