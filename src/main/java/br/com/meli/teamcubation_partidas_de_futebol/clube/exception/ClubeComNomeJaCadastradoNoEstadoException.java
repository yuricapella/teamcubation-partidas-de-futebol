package br.com.meli.teamcubation_partidas_de_futebol.clube.exception;

public class ClubeComNomeJaCadastradoNoEstadoException extends RuntimeException {

    public ClubeComNomeJaCadastradoNoEstadoException() {
        super("Já existe clube com este nome no mesmo estado.");
    }
}
