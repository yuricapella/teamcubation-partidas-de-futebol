package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.CriarClubeRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.CriarClubeRequestMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import br.com.meli.teamcubation_partidas_de_futebol.clube.util.ClubeValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CriarClubeService {
    private final ClubeRepository clubeRepository;
    private final ClubeValidator clubeValidator;
    private final ModelMapper modelMapper;

    public CriarClubeService(ClubeRepository clubeRepository, ClubeValidator clubeValidator, ModelMapper modelMapper) {
        this.clubeRepository = clubeRepository;
        this.clubeValidator = clubeValidator;
        this.modelMapper = modelMapper;
    }

    public ClubeResponseDTO criarClube(CriarClubeRequestDTO clubeACriar) {
        Clube clubeCriado = CriarClubeRequestMapper.toEntity(clubeACriar);
        clubeValidator.validarClubeNaCriacao(clubeCriado);
        clubeRepository.save(clubeCriado);
        return modelMapper.map(clubeCriado, ClubeResponseDTO.class);
    }
}
