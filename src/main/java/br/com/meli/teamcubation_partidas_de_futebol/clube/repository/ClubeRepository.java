package br.com.meli.teamcubation_partidas_de_futebol.clube.repository;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long> {
    boolean existsByNomeAndSiglaEstado(String nome, String siglaEstado);
}
