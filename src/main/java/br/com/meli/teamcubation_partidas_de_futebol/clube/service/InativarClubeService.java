package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import org.springframework.stereotype.Service;

@Service
public class InativarClubeService {
    private final BuscarClubeService buscarClubeService;
    private final ClubeRepository clubeRepository;

    public InativarClubeService(BuscarClubeService buscarClubeService, ClubeRepository clubeRepository) {
        this.buscarClubeService = buscarClubeService;
        this.clubeRepository = clubeRepository;
    }

    public void inativarClubePorId(Long id) {
        Clube clube = buscarClubeService.buscarClubePorId(id);
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }
}
