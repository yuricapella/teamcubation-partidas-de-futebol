package br.com.meli.teamcubation_partidas_de_futebol.clube.service;

import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.clube.dto.mapper.RetrospectosAdversariosMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.RetrospectoContraAdversario;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.RetrospectoTotalClube;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BuscarRetrospectoService {
    private final BuscarClubeService buscarClubeService;
    private final PartidaRepository partidaRepository;

    public BuscarRetrospectoService(BuscarClubeService buscarClubeService, PartidaRepository partidaRepository) {
        this.buscarClubeService = buscarClubeService;
        this.partidaRepository = partidaRepository;
    }

    public RetrospectoTotalClube buscarRetrospectoClube(Long id) {
        Clube clubeRetornado = buscarClubeService.buscarClubePorId(id);
        List<Partida> partidas = partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(id,id);
        RetrospectoTotalClube retrospecto = new RetrospectoTotalClube(clubeRetornado, partidas);
        return retrospecto;
    }

    public RetrospectoAdversariosResponseDTO buscarRetrospectoClubeContraAdversarios(Long id) {
        Clube clube = buscarClubeService.buscarClubePorId(id);
        List<Partida> partidas = partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(id,id);

        Map<Clube, List<Partida>> partidasPorAdversario = partidas.stream()
                .collect(Collectors.groupingBy(
                        partida -> partida.getClubeMandante().getId().equals(clube.getId())
                                ? partida.getClubeVisitante()
                                : partida.getClubeMandante()
                ));

        List<RetrospectoContraAdversario> retrospectos = partidasPorAdversario.entrySet().stream()
                .map(entry -> new RetrospectoContraAdversario(
                        id,
                        entry.getKey(),
                        entry.getValue()))
                .toList();

        return RetrospectosAdversariosMapper.toDTO(clube.getNome(), clube.getSiglaEstado(), retrospectos);
    }

//    public RetrospectoTotalClube buscarRetrospectoClubeContraAdversarios(RetrospectoContraAdversarioRequestDTO dadosDoClubeEAdversario) {
//        Long idClube = dadosDoClubeEAdversario.getClubeId();
//        Long idAdversario = dadosDoClubeEAdversario.getAdversarioId();
//        Clube clubeRetornado = buscarClubeService.buscarClubePorId(idClube);
//        Clube adversarioRetornado = buscarClubeService.buscarClubePorId(idAdversario);
//        List<Partida> partidas = partidaRepository.findPartidasDoClubeContraAdversario(idClube, idAdversario);
//        RetrospectoTotalClube retrospectoContraAdversario =  new RetrospectoTotalClube(clubeRetornado, partidas);
//        return retrospectoContraAdversario;
//    }
}
