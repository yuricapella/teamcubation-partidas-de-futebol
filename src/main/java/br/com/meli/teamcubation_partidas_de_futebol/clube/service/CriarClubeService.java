package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeValidator;
import org.springframework.stereotype.Service;

@Service
public class CriarClubeService {
    private final ClubeRepository clubeRepository;
    private final ClubeValidator clubeValidator;

    public CriarClubeService(ClubeRepository clubeRepository, ClubeValidator clubeValidator) {
        this.clubeRepository = clubeRepository;
        this.clubeValidator = clubeValidator;
    }

    public Clube criarClube(Clube clube) {
        clubeValidator.validarClube(clube);
        return clubeRepository.save(clube);
    }
}
