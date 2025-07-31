package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.CriarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.CriarClubeRequestMapper;
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

    public Clube criarClube(CriarClubeRequestDTO clubeACriar) {
        Clube clubeCriado = CriarClubeRequestMapper.toEntity(clubeACriar);
        clubeValidator.validarClubeNaCriacao(clubeCriado);
        return clubeRepository.save(clubeCriado);
    }
}
