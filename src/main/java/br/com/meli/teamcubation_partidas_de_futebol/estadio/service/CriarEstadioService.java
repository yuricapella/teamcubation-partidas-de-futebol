package br.com.meli.teamcubation_partidas_de_futebol.estadio.service;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CriarEstadioRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.mapper.CriarEstadioRequestMapper;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.repository.EstadioRepository;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.util.EstadioValidator;
import org.springframework.stereotype.Service;

@Service
public class CriarEstadioService {
    private final EstadioRepository estadioRepository;
    private final EstadioValidator estadioValidator;

    public CriarEstadioService(EstadioRepository estadioRepository, EstadioValidator estadioValidator) {
        this.estadioRepository = estadioRepository;
        this.estadioValidator = estadioValidator;
    }

    public Estadio criarEstadio(CriarEstadioRequestDTO estadioACriar) {
        Estadio estadioCriado = CriarEstadioRequestMapper.toEntity(estadioACriar);
        estadioValidator.validarDadosDoEstadio(estadioCriado.getNome());
        return estadioRepository.save(estadioCriado);
    }
}
