package br.com.meli.teamcubation_partidas_de_futebol.ranking.model;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;

public class RankingJogos extends Ranking {

    public RankingJogos(Clube clube, Retrospecto retrospecto) {
        super(clube, retrospecto);
    }

    @Override
    protected int calcularTotal(Clube clube, Retrospecto retrospecto) {
        if(retrospecto.getJogos() > 0 ){
            return retrospecto.getJogos();
        }
        return 0;
    }
}
