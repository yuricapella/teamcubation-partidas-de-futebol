package br.com.meli.teamcubation_partidas_de_futebol.partida.exception;

public class PartidaNaoEncontradaException extends RuntimeException {

    public PartidaNaoEncontradaException(Long id) {
        super("Partida com id " + id + " não encontrada.");
    }
}
