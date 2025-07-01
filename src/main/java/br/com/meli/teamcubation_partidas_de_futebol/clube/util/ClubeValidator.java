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

    public void validarClubeNaCriacao(Clube clube) {
        validarEstado(clube);
        validarNomeAoCriar(clube);
    }

    public void validarClubeNaAtualizacao(Clube clube) {
        validarEstado(clube);
        validarNomeAoAtualizar(clube);
    }

    public void validarEstado(Clube clube) {
        if (!EstadosUF.isValido(clube.getSiglaEstado())) {
            throw new EstadoInexistenteException(clube.getSiglaEstado());
        }
    }

    private void validarNomeAoCriar(Clube clube) {
        if (repository.existsByNomeAndSiglaEstado(clube.getNome(), clube.getSiglaEstado())) {
            throw new ClubeComNomeJaCadastradoNoEstadoException();
        }
    }

    private void validarNomeAoAtualizar(Clube clube) {
        if (repository.existsByNomeAndSiglaEstadoAndIdNot(clube.getNome(), clube.getSiglaEstado(), clube.getId())) {
            throw new ClubeComNomeJaCadastradoNoEstadoException();
        }
    }
}
