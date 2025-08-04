package br.com.meli.teamcubation_partidas_de_futebol.partida.exception;

public class DataPartidaAnteriorACriacaoDoClubeException extends RuntimeException {

    public DataPartidaAnteriorACriacaoDoClubeException() {
        super("Não pode cadastrar uma partida para uma data anterior à data de criação do clube.");
    }
}
