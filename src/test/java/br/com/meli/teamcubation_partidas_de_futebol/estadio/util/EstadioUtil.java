package br.com.meli.teamcubation_partidas_de_futebol.estadio.util;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

public class EstadioUtil {

    public static Estadio criarEstadio(Long id) {
        Estadio estadio = new Estadio("Estadio Exemplo");
        estadio.setId(id);
        estadio.setDataAtualizacao(null);
        return estadio;
    }

    public static List<Estadio> criarEstadios(int quantidade) {
        List<Estadio> estadios = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            Estadio estadio = new Estadio("Estadio " + i);
            estadio.setId((long) i);
            estadio.setDataAtualizacao(null);
            estadios.add(estadio);
        }
        return estadios;
    }

    public static Page<Estadio> criarPageEstadios(int quantidade) {
        List<Estadio> estadios = criarEstadios(quantidade);
        return new PageImpl<>(estadios, PageRequest.of(0, 10), quantidade);
    }

    public static EstadioResponseDTO criarEstadioResponseDTO() {
        return new EstadioResponseDTO("Estadio Exemplo");
    }
}






