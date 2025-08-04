package br.com.meli.teamcubation_partidas_de_futebol.ranking.model;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;

public class RankingPontos extends Ranking {

    public RankingPontos(Clube clube, Retrospecto retrospecto) {
        super(clube, retrospecto);
    }

    @Override
    protected int calcularTotal(Clube clube, Retrospecto retrospecto) {
        if(retrospecto.getVitorias() > 0 || retrospecto.getEmpates() > 0){
            return (retrospecto.getVitorias() * 3) + retrospecto.getEmpates();
        }
        return 0;
    }
}
