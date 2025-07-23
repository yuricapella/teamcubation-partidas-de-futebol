package br.com.meli.teamcubation_partidas_de_futebol.clube.util;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClubeUtil {

    public static Clube criarClube(Long id) {
        Clube clube = new Clube("Clube Exemplo", "AM", true, LocalDate.of(2025, 11, 3));
        clube.setId(id);
        clube.setDataAtualizacao(null);
        return clube;
    }

    public static List<Clube> criarClubes(int quantidade) {
        List<Clube> clubes = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            Clube clube = new Clube("Clube " + i, "AM", true, LocalDate.of(2025, 11, 3));
            clube.setId((long) i);
            clube.setDataAtualizacao(null);
            clubes.add(clube);
        }
        return clubes;
    }

    public static Page<Clube> criarPageClubes(int quantidade) {
        List<Clube> clubes = criarClubes(quantidade);
        return new PageImpl<>(clubes, PageRequest.of(0, 10), quantidade);
    }
}
