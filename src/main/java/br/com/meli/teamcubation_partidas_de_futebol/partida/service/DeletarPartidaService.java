package br.com.meli.teamcubation_partidas_de_futebol.partida.service;

import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import br.com.meli.teamcubation_partidas_de_futebol.partida.repository.PartidaRepository;
import org.springframework.stereotype.Service;

@Service
public class DeletarPartidaService {
    private final PartidaRepository partidaRepository;
    private final BuscarPartidaService buscarPartidaService;

    public DeletarPartidaService(PartidaRepository partidaRepository, BuscarPartidaService buscarPartidaService) {
        this.partidaRepository = partidaRepository;
        this.buscarPartidaService = buscarPartidaService;
    }

    public void deletarPartidaPorId(Long id) {
        Partida partidaEncontrada = buscarPartidaService.buscarPartidaPorId(id);
        partidaRepository.delete(partidaEncontrada);
    }
}
