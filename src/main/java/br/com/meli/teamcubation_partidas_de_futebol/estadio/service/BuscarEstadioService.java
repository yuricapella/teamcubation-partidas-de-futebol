package br.com.meli.teamcubation_partidas_de_futebol.estadio.service;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.repository.EstadioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarEstadioService {
    EstadioRepository estadioRepository;

    public BuscarEstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    public Estadio buscarEstadioPorId(Long id) {
        Optional<Estadio> estadioOptional = estadioRepository.findById(id);
        return estadioOptional.orElseThrow(() -> new EstadioNaoEncontradoException(id));
    }
}
