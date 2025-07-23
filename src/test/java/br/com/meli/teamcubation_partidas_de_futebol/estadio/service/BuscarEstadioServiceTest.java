package br.com.meli.teamcubation_partidas_de_futebol.estadio.service;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.repository.EstadioRepository;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.util.EstadioUtil;
import br.com.meli.teamcubation_partidas_de_futebol.util.PrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class BuscarEstadioServiceTest {
    BuscarEstadioService buscarEstadioService;
    EstadioRepository estadioRepository;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        estadioRepository = Mockito.mock(EstadioRepository.class);
        buscarEstadioService = new BuscarEstadioService(estadioRepository);
    }

    @ParameterizedTest
    @CsvSource({
        "Estadio de time",

        ","
    })
    void deveListarEstadiosFiltradosComSucesso(String nomeCsv) {
        String nome = (nomeCsv != null && !nomeCsv.isBlank()) ? nomeCsv : null;

        Page<Estadio> estadiosPage = EstadioUtil.criarPageEstadios(2);

        Mockito.when(estadioRepository.findByFiltros(
                Mockito.eq(nome),
                Mockito.any(Pageable.class))
        ).thenReturn(estadiosPage);

        Page<EstadioResponseDTO> estadiosPageDTO = buscarEstadioService.listarEstadiosFiltrados(
                nome,
                PageRequest.of(0, 10));

        Assertions.assertEquals(2, estadiosPageDTO.getTotalElements());
        Assertions.assertEquals(estadiosPage.getContent().getFirst().getNome(), estadiosPageDTO.getContent().getFirst().getNome());

        Mockito.verify(estadioRepository, Mockito.times(1)).findByFiltros(
                Mockito.eq(nome),
                Mockito.any(Pageable.class)
        );
    }

    @Test
    void deveBuscarEstadioPorIdComSucesso() {
        Long id = 1L;
        Estadio estadio = EstadioUtil.criarEstadio(id);
        Optional<Estadio> estadioOptional = Optional.of(estadio);

        Mockito.when(estadioRepository.findById(id)).thenReturn(estadioOptional);

        Estadio estadioEncontrado = buscarEstadioService.buscarEstadioPorId(id);

        Assertions.assertNotNull(estadioEncontrado);
        Assertions.assertEquals(estadio, estadioEncontrado);
        Assertions.assertEquals(id, estadioEncontrado.getId());
        Assertions.assertEquals(estadio.getNome(), estadioEncontrado.getNome());
        Assertions.assertEquals(estadio.getDataAtualizacao(), estadioEncontrado.getDataAtualizacao());

        Mockito.verify(estadioRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void deveLancarEstadioNaoEncontradoException_quandoEstadioNaoExistir() {
        Long id = 1L;

        Mockito.when(estadioRepository.findById(id)).thenReturn(Optional.empty());

        EstadioNaoEncontradoException exception = Assertions.assertThrows(
                EstadioNaoEncontradoException.class, () -> buscarEstadioService.buscarEstadioPorId(id)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Estadio com id " + id + " não encontrado.", exception.getMessage());
        Mockito.verify(estadioRepository, Mockito.times(1)).findById(id);

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }
}
