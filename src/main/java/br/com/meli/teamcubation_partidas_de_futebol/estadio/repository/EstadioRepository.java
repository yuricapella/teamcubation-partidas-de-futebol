package br.com.meli.teamcubation_partidas_de_futebol.estadio.repository;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}
