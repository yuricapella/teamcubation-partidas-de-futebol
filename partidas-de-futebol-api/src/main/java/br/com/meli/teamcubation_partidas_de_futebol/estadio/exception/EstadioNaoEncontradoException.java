package br.com.meli.teamcubation_partidas_de_futebol.estadio.exception;

public class EstadioNaoEncontradoException extends RuntimeException {

    public EstadioNaoEncontradoException(Long id) {
        super("Estadio com id " + id + " não encontrado.");
    }
}
