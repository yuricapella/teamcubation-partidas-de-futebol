package br.com.meli.teamcubation_partidas_de_futebol.estadio.service;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioEnderecoResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper.EstadioResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.repository.EstadioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarEstadioService {
    private final EstadioRepository estadioRepository;
    private final EnderecoViaCepClient enderecoViaCepClient;

    public BuscarEstadioService(EstadioRepository estadioRepository, EnderecoViaCepClient enderecoViaCepClient) {
        this.estadioRepository = estadioRepository;
        this.enderecoViaCepClient = enderecoViaCepClient;
    }

    public EstadioEnderecoResponseDTO buscarEstadioComEnderecoPorId(Long id) {
        Optional<Estadio> estadioOptional = estadioRepository.findById(id);
        estadioOptional.orElseThrow(() -> new EstadioNaoEncontradoException(id));
        Estadio estadio = estadioOptional.get();
        return enderecoViaCepClient.criarEstadioEnderecoResponseDTO(estadio.getNome(), estadio.getCep());
    }

    public Estadio buscarEstadioPorId(Long id) {
        Optional<Estadio> estadioOptional = estadioRepository.findById(id);
        return estadioOptional.orElseThrow(() -> new EstadioNaoEncontradoException(id));
    }

    public Page<EstadioResponseDTO> listarEstadiosFiltrados (String nome, Pageable pageable) {
        return estadioRepository.findByFiltros(nome, pageable)
                .map(EstadioResponseMapper::toEstadioResponseDTO);
    }
}
