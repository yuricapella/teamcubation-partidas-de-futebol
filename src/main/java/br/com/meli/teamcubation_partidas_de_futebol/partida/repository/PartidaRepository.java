package br.com.meli.teamcubation_partidas_de_futebol.partida.repository;

import br.com.meli.teamcubation_partidas_de_futebol.partida.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PartidaRepository extends JpaRepository<Partida, Integer> {
    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    FROM partida
    WHERE (clube_mandante_id IN (:clubeMandanteId, :clubeVisitanteId)
           OR clube_visitante_id IN (:clubeMandanteId, :clubeVisitanteId))
      AND ABS(TIMESTAMPDIFF(hour, data_hora, :dataNovaPartida)) < 48
    """, nativeQuery = true)
    Long existeConflitoDeHorario(
            @Param("clubeMandanteId") Long clubeMandanteId,
            @Param("clubeVisitanteId") Long clubeVisitanteId,
            @Param("dataNovaPartida") LocalDateTime dataNovaPartida);

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    FROM partida 
    WHERE estadio_id = :estadioId 
    AND DATE(data_hora) = :data
    """, nativeQuery = true)
    Long existePartidaNoEstadioNoMesmoDia(Long estadioId, LocalDate data);
}
