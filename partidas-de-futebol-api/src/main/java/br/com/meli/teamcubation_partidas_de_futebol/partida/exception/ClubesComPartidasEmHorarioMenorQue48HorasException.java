package br.com.meli.teamcubation_partidas_de_futebol.partida.exception;

public class ClubesComPartidasEmHorarioMenorQue48HorasException extends RuntimeException {

    public ClubesComPartidasEmHorarioMenorQue48HorasException() {
        super("Não é possível criar a partida pois um dos clubes já possui uma partida cadastrada em menos de 48 horas desta data.");
    }
}
