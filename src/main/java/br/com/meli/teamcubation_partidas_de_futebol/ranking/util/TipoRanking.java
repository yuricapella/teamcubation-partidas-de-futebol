package br.com.meli.teamcubation_partidas_de_futebol.ranking.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

public enum TipoRanking {
    TOTAL_PONTOS,
    TOTAL_GOLS,
    TOTAL_VITORIAS,
    TOTAL_JOGOS;

    public static TipoRanking fromString(String valor) {
        try {
            return TipoRanking.valueOf(valor.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "tipoRanking inválido! Use valores: TOTAL_PONTOS, TOTAL_GOLS, TOTAL_VITORIAS, TOTAL_JOGOS."
            );
        }
    }

}

