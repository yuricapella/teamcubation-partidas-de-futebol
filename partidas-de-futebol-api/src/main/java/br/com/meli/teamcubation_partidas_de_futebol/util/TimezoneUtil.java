package br.com.meli.teamcubation_partidas_de_futebol.util;

import java.util.Arrays;
import java.util.List;

public class TimezoneUtil {
    public static final String ZONE_SAO_PAULO = "America/Sao_Paulo";

    public static String getZoneSaoPaulo() {
        return ZONE_SAO_PAULO;
    }

    public static List<String> getSupportedZones() {
        return Arrays.asList(
                ZONE_SAO_PAULO
        );
    }
}
