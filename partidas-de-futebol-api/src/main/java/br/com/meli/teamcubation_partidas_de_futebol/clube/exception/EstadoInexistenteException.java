package br.com.meli.teamcubation_partidas_de_futebol.clube.exception;

public class EstadoInexistenteException extends RuntimeException {

    public EstadoInexistenteException(String siglaEstado) {
        super("Não é possivel criar o clube pois o estado " +siglaEstado+ " não existe.");
    }
}
