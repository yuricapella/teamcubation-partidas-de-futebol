package br.com.meli.teamcubation_partidas_de_futebol.partida.exception;

public class ClubesIguaisException extends RuntimeException {

    public ClubesIguaisException() {
        super("Não é possivel criar a partida pois os clubes são iguais.");
    }
}
