package br.com.meli.teamcubation_partidas_de_futebol.estadio.exception;

public class EstadioJaExisteException extends RuntimeException {

    public EstadioJaExisteException() {
        super("Já existe um estadio com este nome.");
    }
}
