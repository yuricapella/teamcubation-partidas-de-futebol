package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.AtualizarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.AtualizarClubeRequestMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeValidator;
import org.springframework.stereotype.Service;

@Service
public class AtualizarClubeService {
    private final BuscarClubeService buscarClubeService;
    private final ClubeRepository clubeRepository;
    private final ClubeValidator clubeValidator;


    public AtualizarClubeService(BuscarClubeService buscarClubeService, ClubeRepository clubeRepository, ClubeValidator clubeValidator) {
        this.buscarClubeService = buscarClubeService;
        this.clubeRepository = clubeRepository;
        this.clubeValidator = clubeValidator;
    }

    public Clube atualizar(AtualizarClubeRequestDTO clubeAtualizado, Long id){
        Clube clubeJaCadastrado = buscarClubeService.buscarClubePorId(id);
        AtualizarClubeRequestMapper.updateEntity(clubeAtualizado, clubeJaCadastrado);
        clubeValidator.validarClubeNaAtualizacao(clubeJaCadastrado);
        return clubeRepository.save(clubeJaCadastrado);
    }
}
