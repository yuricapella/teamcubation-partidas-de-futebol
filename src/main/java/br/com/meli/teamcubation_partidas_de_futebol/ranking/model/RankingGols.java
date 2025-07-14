package br.com.meli.teamcubation_partidas_de_futebol.ranking.model;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;

public class RankingGols extends Ranking {

    public RankingGols(Clube clube, Retrospecto retrospecto) {
        super(clube, retrospecto);
    }

    @Override
    protected int calcularTotal(Clube clube, Retrospecto retrospecto) {
        if(retrospecto.getGolsFeitos() > 0 ){
            return retrospecto.getGolsFeitos();
        }
        return 0;
    }
}
