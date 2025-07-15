package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.service;

import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.mapper.RetrospectosAdversariosMapper;
import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.BuscarClubeService;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.PartidaResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.partida.dto.mapper.PartidaResponseMapper;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoAdversariosResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto.RetrospectoConfrontoRequestDTO;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.Retrospecto;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoAdversario;
import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoConfronto;
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

    public Retrospecto buscarRetrospectoClube(Long id, Boolean mandante, Boolean visitante) {
        Clube clubeRetornado = buscarClubeService.buscarClubePorId(id);
        List<Partida> partidas = partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(id,id);
        List<Partida> partidasFiltradas = filtrarPartidasPorMandanteVisitante(partidas,id, mandante, visitante);
        return new Retrospecto(clubeRetornado, partidasFiltradas);
    }

    public RetrospectoAdversariosResponseDTO buscarRetrospectoClubeContraAdversarios
            (Long id, Boolean mandante, Boolean visitante) {

        Clube clube = buscarClubeService.buscarClubePorId(id);
        List<Partida> partidas = partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(id,id);
        List<Partida> partidasFiltradas = filtrarPartidasPorMandanteVisitante(partidas,id, mandante, visitante);

        Map<Clube, List<Partida>> partidasPorAdversario = partidasFiltradas.stream()
                .collect(Collectors.groupingBy(
                        partida -> partida.getClubeMandante().getId().equals(clube.getId())
                                ? partida.getClubeVisitante()
                                : partida.getClubeMandante()
                ));

        List<RetrospectoAdversario> retrospectos = partidasPorAdversario.entrySet().stream()
                .map(entry -> new RetrospectoAdversario(
                        id,
                        entry.getKey(),
                        entry.getValue()))
                .toList();

        return RetrospectosAdversariosMapper.toDTO(clube.getNome(), clube.getSiglaEstado(), retrospectos);
    }

    public RetrospectoConfronto buscarRetrospectoConfronto(RetrospectoConfrontoRequestDTO dto) {
        Long idClube = dto.getClubeId();
        Long idAdversario = dto.getAdversarioId();

        Clube clube = buscarClubeService.buscarClubePorId(idClube);
        Clube clubeAdversario = buscarClubeService.buscarClubePorId(idAdversario);

        List<Partida> partidas = partidaRepository.findPartidasDoClubeContraAdversario(idClube, idAdversario);
        List<PartidaResponseDTO> partidasDTO = PartidaResponseMapper.toPartidaResponseDTO(partidas);

        Retrospecto retrospectoClube = new Retrospecto(clube, partidas);
        Retrospecto retrospectoAdversario = new Retrospecto(clubeAdversario, partidas);
        return new RetrospectoConfronto(List.of(retrospectoClube, retrospectoAdversario), partidasDTO);
    }

    private List<Partida> filtrarPartidasPorMandanteVisitante(List<Partida> partidas, Long id, Boolean mandante, Boolean visitante) {
        return partidas.stream()
                .filter(partida -> mandante == null || (partida.getClubeMandante().getId().equals(id) == mandante))
                .filter(partida -> visitante == null || (partida.getClubeVisitante().getId().equals(id) == visitante))
                .collect(Collectors.toList());
    }
}
