package br.com.meli.teamcubation_partidas_de_futebol.estadio.util;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioJaExisteException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.repository.EstadioRepository;
import org.springframework.stereotype.Component;

@Component
public class EstadioValidator {
    private final EstadioRepository estadioRepository;

    public EstadioValidator(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    public void validarDadosDoEstadio(String nomeACadastrar) {
        validarEstadioJaExistePeloNome(nomeACadastrar);
    }

    public void validarEstadioJaExistePeloNome(String nomeACadastrar){
        if(estadioRepository.existsByNomeIgnoreCase(nomeACadastrar)){
           throw new EstadioJaExisteException();
        }
    }
}
