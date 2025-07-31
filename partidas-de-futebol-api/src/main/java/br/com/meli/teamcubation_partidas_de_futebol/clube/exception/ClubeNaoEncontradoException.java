package br.com.meli.teamcubation_partidas_de_futebol.clube.exception;

public class ClubeNaoEncontradoException extends RuntimeException {

    public ClubeNaoEncontradoException(Long id) {
        super("Clube com id " + id + " não encontrado.");
    }
}
