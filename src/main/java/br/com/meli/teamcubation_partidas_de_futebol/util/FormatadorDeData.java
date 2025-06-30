package br.com.meli.teamcubation_partidas_de_futebol.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatadorDeData {
    public static final String PADRAO_DATA_HORA = "dd/MM/yyyy HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PADRAO_DATA_HORA);

    public static String formatarDataHora(LocalDateTime data) {
        return data.format(FORMATTER);
    }
}
