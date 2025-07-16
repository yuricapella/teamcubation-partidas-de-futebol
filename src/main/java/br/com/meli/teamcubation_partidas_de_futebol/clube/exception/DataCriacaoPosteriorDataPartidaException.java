package br.com.meli.teamcubation_partidas_de_futebol.clube.exception;

public class DataCriacaoPosteriorDataPartidaException extends RuntimeException {
    public DataCriacaoPosteriorDataPartidaException(Long id) {
        super("A data de criação do clube com id " + id + " está posterior a data de uma partida cadastrada.");
    }
}
