package br.com.meli.teamcubation_partidas_de_futebol.estadio.service;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.repository.EstadioRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarEstadioService {
    EstadioRepository estadioRepository;

    public CriarEstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    public Estadio criarEstadio(Estadio estadio) {
        return estadioRepository.save(estadio);
    }
}
