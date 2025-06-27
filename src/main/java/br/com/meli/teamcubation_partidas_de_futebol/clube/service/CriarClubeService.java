package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarClubeService {
    private ClubeRepository clubeRepository;

    public CriarClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public Clube criarClube(Clube clube) {
        return clubeRepository.save(clube);
    }
}
