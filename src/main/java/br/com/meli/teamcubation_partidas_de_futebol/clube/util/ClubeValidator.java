package br.com.meli.teamcubation_partidas_de_futebol.clube.util;

import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeComNomeJaCadastradoNoEstadoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.EstadoInexistenteException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import org.springframework.stereotype.Component;

@Component
public class ClubeValidator {
    private final  ClubeRepository repository;

    public ClubeValidator(ClubeRepository repository) {
        this.repository = repository;
    }

    public void validarClube(Clube clube) {
        validarEstado(clube);
        validarNome(clube);
    }

    public void validarEstado(Clube clube) {
        if (!EstadosUF.isValido(clube.getSiglaEstado())) {
            throw new EstadoInexistenteException(clube.getSiglaEstado());
        }
    }

    public void validarNome(Clube clube) {
        if(repository.existsByNomeAndSiglaEstado
                (clube.getNome(), clube.getSiglaEstado())){
            throw new ClubeComNomeJaCadastradoNoEstadoException();
        }
    }
}
