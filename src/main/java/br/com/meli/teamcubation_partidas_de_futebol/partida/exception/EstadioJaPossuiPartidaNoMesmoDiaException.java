package br.com.meli.teamcubation_partidas_de_futebol.partida.exception;

public class EstadioJaPossuiPartidaNoMesmoDiaException extends RuntimeException {

    public EstadioJaPossuiPartidaNoMesmoDiaException() {
        super("Não é possivel criar a partida pois no estádio já tem uma partida marcada para o mesmo dia");
    }
}
