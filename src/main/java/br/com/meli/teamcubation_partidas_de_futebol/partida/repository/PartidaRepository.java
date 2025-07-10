package br.com.meli.teamcubation_partidas_de_futebol.partida.repository;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    FROM partida
    WHERE (clube_mandante_id IN (:clubeMandanteId, :clubeVisitanteId)
           OR clube_visitante_id IN (:clubeMandanteId, :clubeVisitanteId))
      AND ABS(TIMESTAMPDIFF(hour, data_hora, :dataNovaPartida)) < 48
    """, nativeQuery = true)
    int countConflitosDeHorarioNosClubes(
            @Param("clubeMandanteId") Long clubeMandanteId,
            @Param("clubeVisitanteId") Long clubeVisitanteId,
            @Param("dataNovaPartida") LocalDateTime dataNovaPartida);

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    FROM partida 
    WHERE estadio_id = :estadioId 
    AND DATE(data_hora) = :data
    """, nativeQuery = true)
    int countPartidasNoEstadioNoDia(Long estadioId, LocalDate data);


    @Query("""
    SELECT partida FROM Partida partida
    WHERE
      (
        (:clubeId IS NULL OR partida.clubeMandante.id = :clubeId OR partida.clubeVisitante.id = :clubeId)
      )
      AND (:estadioId IS NULL OR partida.estadio.id = :estadioId)
    """)
    Page<Partida> findByFiltros(
            @Param("clubeId") Long clubeId,
            @Param("estadioId") Long estadioId,
            Pageable pageable
    );

    List<Partida> findByClubeMandanteIdOrClubeVisitanteId(Long clubeIdMandante, Long clubeIdVisitante);
}
