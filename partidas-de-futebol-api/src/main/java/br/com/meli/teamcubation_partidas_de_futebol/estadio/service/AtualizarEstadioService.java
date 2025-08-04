package br.com.meli.teamcubation_partidas_de_futebol.estadio.service;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.AtualizarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioEnderecoResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper.AtualizarEstadioRequestMapper;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.repository.EstadioRepository;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.util.EstadioValidator;
import org.springframework.stereotype.Service;

@Service
public class AtualizarEstadioService {
    private final EstadioRepository estadioRepository;
    private final EstadioValidator estadioValidator;
    private final BuscarEstadioService buscarEstadioService;
    private final EnderecoViaCepClient enderecoViaCepClient;

    public AtualizarEstadioService(EstadioRepository estadioRepository, EstadioValidator estadioValidator, BuscarEstadioService buscarEstadioService, EnderecoViaCepClient enderecoViaCepClient) {
        this.estadioRepository = estadioRepository;
        this.estadioValidator = estadioValidator;
        this.buscarEstadioService = buscarEstadioService;
        this.enderecoViaCepClient = enderecoViaCepClient;
    }

    public EstadioEnderecoResponseDTO atualizarEstadio(AtualizarEstadioRequestDTO dadosParaAtualizar, Long id) {
        Estadio dadosAntigos = buscarEstadioService.buscarEstadioPorId(id);
        estadioValidator.validarDadosDoEstadioAoAtualizar(dadosParaAtualizar.getNome(), dadosAntigos.getNome());
        Estadio estadioAtualizado = AtualizarEstadioRequestMapper.updateEntity(dadosParaAtualizar, dadosAntigos);
        estadioRepository.save(estadioAtualizado);
        return enderecoViaCepClient.criarEstadioEnderecoResponseDTO(estadioAtualizado.getNome(),estadioAtualizado.getCep());
    }
}
