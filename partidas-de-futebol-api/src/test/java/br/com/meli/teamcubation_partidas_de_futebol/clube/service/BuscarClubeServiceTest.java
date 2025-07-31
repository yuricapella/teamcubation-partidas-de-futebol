package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeUtil;
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

public class BuscarClubeServiceTest {
    BuscarClubeService buscarClubeService;
    ClubeRepository clubeRepository;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        PrintUtil.printInicioDoTeste(testInfo.getDisplayName());
        clubeRepository = Mockito.mock(ClubeRepository.class);
        buscarClubeService = new BuscarClubeService(clubeRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "Nome,,",
            ",AM,",
            ",,true"
    })
    void deveListarClubesFiltradosComSucesso(String nomeCsv, String estadoCsv, String ativoCsv){
        String nome = (nomeCsv != null && !nomeCsv.isBlank()) ? nomeCsv : null;
        String estado = (estadoCsv != null && !estadoCsv.isBlank()) ? estadoCsv : null;
        Boolean ativo = (ativoCsv != null && !ativoCsv.isBlank()) ? Boolean.valueOf(ativoCsv) : null;

        Page<Clube> clubesPage = ClubeUtil.criarPageClubes(2);

        Mockito.when(clubeRepository.findByFiltros(
                Mockito.eq(nome),
                Mockito.eq(estado),
                Mockito.eq(ativo),
                Mockito.any(Pageable.class))
        ).thenReturn(clubesPage);

        Page<ClubeResponseDTO> clubesPageDTO = buscarClubeService.listarClubesFiltrados(
                nome,
                estado,
                ativo,
                PageRequest.of(0, 10));

        Assertions.assertEquals(2, clubesPageDTO.getTotalElements());
        Assertions.assertEquals(clubesPage.getContent().getFirst().getNome(), clubesPageDTO.getContent().getFirst().getNome());
        Assertions.assertEquals(clubesPage.getContent().getFirst().getSiglaEstado(), clubesPageDTO.getContent().getFirst().getSiglaEstado());
        Assertions.assertEquals(clubesPage.getContent().getFirst().getDataCriacao(), clubesPageDTO.getContent().getFirst().getDataCriacao());

        Mockito.verify(clubeRepository, Mockito.times(1)).findByFiltros(
                Mockito.eq(nome),
                Mockito.eq(estado),
                Mockito.eq(ativo),
                Mockito.any(Pageable.class)
        );
    }

    @Test
    void deveBuscarClubePorIdComSucesso(){
        Long id = 1L;
        Clube clube = ClubeUtil.criarClube(id);
        Optional<Clube> clubeOptinal = Optional.of(clube);

        Mockito.when(clubeRepository.findById(id)).thenReturn(clubeOptinal);

        Clube clubeEncontrado = buscarClubeService.buscarClubePorId(id);

        Assertions.assertNotNull(clubeEncontrado);
        Assertions.assertEquals(clube, clubeEncontrado);
        Assertions.assertEquals(id, clubeEncontrado.getId());
        Assertions.assertEquals(clube.getNome(), clubeEncontrado.getNome());
        Assertions.assertEquals(clube.getSiglaEstado(), clubeEncontrado.getSiglaEstado());
        Assertions.assertEquals(clube.getDataCriacao(), clubeEncontrado.getDataCriacao());
        Assertions.assertEquals(clube.getAtivo(), clubeEncontrado.getAtivo());
        Assertions.assertEquals(clube.getDataAtualizacao(), clubeEncontrado.getDataAtualizacao());
        Mockito.verify(clubeRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void deveLancarClubeNaoEncontradoException_quandoClubeNaoExistir(){
        Long id = 1L;

        Mockito.when(clubeRepository.findById(id)).thenReturn(Optional.empty());

        ClubeNaoEncontradoException exception = Assertions.assertThrows(
                ClubeNaoEncontradoException.class, () -> buscarClubeService.buscarClubePorId(id)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Clube com id " + id + " não encontrado.", exception.getMessage());
        Mockito.verify(clubeRepository, Mockito.times(1)).findById(id);

        PrintUtil.printMensagemDeErro(exception.getMessage());
    }
}
