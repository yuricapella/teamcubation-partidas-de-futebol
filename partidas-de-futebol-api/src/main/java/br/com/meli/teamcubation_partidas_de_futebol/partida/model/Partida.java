package br.com.meli.teamcubation_partidas_de_futebol.partida.model;

import br.com.meli.teamcubation_partidas_de_futebol.clube.model.Clube;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.model.Estadio;
import br.com.meli.teamcubation_partidas_de_futebol.partida.util.Resultado;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PARTIDA")
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Estadio estadio;

    @ManyToOne
    private Clube clubeMandante;

    @ManyToOne
    private Clube clubeVisitante;

    private int golsMandante;
    private int golsVisitante;
    private LocalDateTime dataHora;
    private LocalDateTime dataAtualizacao;

    public Partida() {
    }

    public Partida(Estadio estadio, Clube clubeMandante, Clube clubeVisitante, int golsMandante, int golsVisitante, LocalDateTime dataHora) {
        this.estadio = estadio;
        this.clubeMandante = clubeMandante;
        this.clubeVisitante = clubeVisitante;
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
        this.dataHora = dataHora;
    }

    public Resultado getResultado() {
        if (this.golsMandante > this.golsVisitante) return Resultado.VITORIA_MANDANTE;
        if (this.golsMandante < this.golsVisitante) return Resultado.VITORIA_VISITANTE;
        return Resultado.EMPATE;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    public Clube getClubeMandante() {
        return clubeMandante;
    }

    public void setClubeMandante(Clube clubeMandante) {
        this.clubeMandante = clubeMandante;
    }

    public Clube getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(Clube clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
    }

    public int getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(int golsMandante) {
        this.golsMandante = golsMandante;
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(int golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public boolean isGoleada() {
        return Math.abs(this.golsMandante - this.golsVisitante) >= 3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
