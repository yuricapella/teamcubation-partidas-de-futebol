package br.com.meli.teamcubation_partidas_de_futebol.estadio.repository;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, Long> {
    boolean existsByNomeIgnoreCase(String nome);

    @Query("""
    SELECT estadio FROM Estadio estadio
    WHERE (:nome IS NULL OR estadio.nome = :nome)
    """)
    Page<Estadio> findByFiltros(@Param("nome") String nome, Pageable pageable);
}
