package br.com.meli.teamcubation_partidas_de_futebol.partida.exception;

public class ClubeInativoException extends RuntimeException {

    public ClubeInativoException() {
        super("Não é possivel criar a partida pois há um clube inativo");
    }
}
