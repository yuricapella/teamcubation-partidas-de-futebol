package br.com.meli.teamcubation_partidas_de_futebol.clube.repository;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long> {
    boolean existsByNomeAndSiglaEstado(String nome, String siglaEstado);
    boolean existsByNomeAndSiglaEstadoAndIdNot(String nome, String siglaEstado, Long id);

    @Query("""
    SELECT clube FROM Clube clube
    WHERE (:nome IS NULL OR UPPER(clube.nome) LIKE UPPER(CONCAT('%', :nome, '%')))
      AND (:estado IS NULL OR UPPER(clube.siglaEstado) = UPPER(:estado))
      AND (:ativo IS NULL OR clube.ativo = :ativo)
    """)
    Page<Clube> findByFiltros(
            @Param("nome") String nome,
            @Param("estado") String estado,
            @Param("ativo") Boolean ativo,
            Pageable pageable
    );

}
