package br.com.meli.teamcubation_partidas_de_futebol.ranking.model;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item de ranking de clubes, contendo nome do clube, UF e total computado (pontos, gols, vitórias ou jogos)")
public abstract class Ranking {
    @Schema(description = "Nome do clube", example = "Clube de Exemplo Um")
    private final String nomeClube;

    @Schema(description = "Sigla do estado do clube", example = "AM")
    private final String estadoClube;

    @Schema(description = "Total do ranking (pontos, gols, vitórias ou jogos)", example = "10")
    private final int total;

    public Ranking(Clube clube, Retrospecto retrospecto) {
        this.nomeClube = clube.getNome();
        this.estadoClube = clube.getSiglaEstado();
        this.total = calcularTotal(clube, retrospecto);
    }

    protected abstract int calcularTotal(Clube clube, Retrospecto retrospecto);

    public String getNomeClube() {
        return nomeClube;
    }

    public String getEstadoClube() {
        return estadoClube;
    }

    public int getTotal() {
        return total;
    }
}
