package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.ClubeResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.ClubeResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.repository.ClubeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuscarClubeService {
    ClubeRepository clubeRepository;

    public BuscarClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public Clube buscarClubePorId(Long id) {
        Optional<Clube> clubeOptinal = clubeRepository.findById(id);
        return clubeOptinal.orElseThrow(()  -> new ClubeNaoEncontradoException(id));
    }

    public Page<ClubeResponseDTO> listarClubesFiltrados(String nome, String estado, Boolean ativo, Pageable pageable) {
        return clubeRepository.findByFiltros(nome,estado,ativo,pageable)
                .map(ClubeResponseMapper::toClubeResponseDTO);
    }
}
