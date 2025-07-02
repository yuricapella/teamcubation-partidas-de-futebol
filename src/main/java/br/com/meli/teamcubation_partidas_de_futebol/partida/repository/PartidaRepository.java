package br.com.meli.teamcubation_partidas_de_futebol.partida.repository;

import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, Integer> {

}
