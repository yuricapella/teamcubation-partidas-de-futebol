package br.com.meli.teamcubation_partidas_de_futebol.clube.controller;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.ClubeResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeApiExceptionHandler;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class BuscarClubeApiControllerTest {
    @InjectMocks
    BuscarClubeApiController buscarClubeApiController;
    @Mock
    BuscarClubeService buscarClubeService;

    MockMvc mockMvc;

    final String PATH = "/api/clube/buscar";
    final String PATH_COM_ID = "/api/clube/buscar/{id}";

    @BeforeEach
    public void setUp(TestInfo testeInfo) {
        PrintUtil.printInicioDoTeste(testeInfo.getDisplayName());
        mockMvc = MockMvcBuilders.standaloneSetup(buscarClubeApiController)
                .setControllerAdvice(new GlobalApiExceptionHandler(), new ClubeApiExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @ParameterizedTest
    @CsvSource({
            ",,",
            "clube de time,,",
            ",sp,",
            ",,true",
            "clube de time,sp,true"
    })
    void deveBuscarTodosClubesComSucesso(String nome, String estado, Boolean ativo) throws Exception {
        ClubeResponseDTO dto1 = new ClubeResponseDTO("clube de time", "sp", LocalDate.of(2025,11,3));
        ClubeResponseDTO dto2 = new ClubeResponseDTO("clube de time", "am", LocalDate.of(2025,11,3));

        Page<ClubeResponseDTO> clubesRetornados = new PageImpl<>(new ArrayList<>(List.of(dto1, dto2)),
                org.springframework.data.domain.PageRequest.of(0, 10),
                2L);


        Mockito.when(buscarClubeService.listarClubesFiltrados(
                any(), any(), any(), any(Pageable.class)
        )).thenReturn(clubesRetornados);

        var requestBuilder = get(PATH)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON);

        if (nome != null && !nome.isEmpty()) requestBuilder = requestBuilder.param("nome", nome);
        if (estado != null && !estado.isEmpty()) requestBuilder = requestBuilder.param("estado", estado);
        if (ativo != null) requestBuilder = requestBuilder.param("ativo", ativo.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(clubesRetornados.getContent().size()))
                .andExpect(jsonPath("$.content[0].nome").value("clube de time"))
                .andExpect(jsonPath("$.content[1].siglaEstado").value("am"))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarClubeService, Mockito.times(1)).listarClubesFiltrados(
                any(), any(), any(), any(Pageable.class)
        );
    }

    @Test
    void deveBuscarClubePorIdComSucesso() throws Exception {
        Long id = 1L;
        Clube clube = new Clube("clube de time","AM",true,LocalDate.of(2025,11,3));
        clube.setId(id);
        ClubeResponseDTO clubeDTO = ClubeResponseMapper.toClubeResponseDTO(clube);

        Mockito.when(buscarClubeService.buscarClubePorId(id)).thenReturn(clube);

        mockMvc.perform(get(PATH_COM_ID,id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.nome").value(clubeDTO.getNome()))
                .andExpect(jsonPath("$.siglaEstado").value(clubeDTO.getSiglaEstado()))
                .andExpect(jsonPath("$.dataCriacao[0]").value(2025))
                .andExpect(jsonPath("$.dataCriacao[1]").value(11))
                .andExpect(jsonPath("$.dataCriacao[2]").value(3))
                .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
    }

    @Test
    void deveRetornarNotFoundQuandoClubeNaoExistir() throws Exception {
        Long id = 999L;
        Mockito.when(buscarClubeService.buscarClubePorId(id))
                .thenThrow(new ClubeNaoEncontradoException(id));

        mockMvc.perform(get(PATH_COM_ID,id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
            .andDo(MockMvcResultHandlers.print());

        Mockito.verify(buscarClubeService, Mockito.times(1)).buscarClubePorId(id);
    }


}
