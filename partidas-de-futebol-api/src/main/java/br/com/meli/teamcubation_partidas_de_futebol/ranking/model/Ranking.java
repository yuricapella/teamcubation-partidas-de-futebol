package br.com.meli.teamcubation_partidas_de_futebol.ranking.model;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;

public abstract class Ranking {
    private final String nomeClube;
    private final String estadoClube;
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
