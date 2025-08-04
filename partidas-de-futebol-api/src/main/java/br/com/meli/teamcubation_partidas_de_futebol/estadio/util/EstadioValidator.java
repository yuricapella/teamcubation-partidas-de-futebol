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

    public void validarDadosDoEstadioAoCriar(String nomeACadastrar) {
        validarEstadioJaExistePeloNome(nomeACadastrar);
    }

    public void validarDadosDoEstadioAoAtualizar(String nomeACadastrar, String nomeAntigo) {
        validarEstadioJaExistePeloNome(nomeACadastrar, nomeAntigo);
    }

    public void validarEstadioJaExistePeloNome(String nomeACadastrar){
        if(estadioRepository.existsByNomeIgnoreCase(nomeACadastrar)){
           throw new EstadioJaExisteException();
        }
    }

    public void validarEstadioJaExistePeloNome(String nomeACadastrar, String nomeAntigo){
        if(!nomeACadastrar.equals(nomeAntigo)){
            validarEstadioJaExistePeloNome(nomeACadastrar);
        }
    }
}
