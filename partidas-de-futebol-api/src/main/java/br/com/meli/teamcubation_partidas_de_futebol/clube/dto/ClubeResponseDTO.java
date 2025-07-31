package br.com.meli.teamcubation_partidas_de_futebol.clube.dto;

import java.time.LocalDate;

public class ClubeResponseDTO {
    private String nome;
    private String siglaEstado;
    private LocalDate dataCriacao;

    public ClubeResponseDTO() {
    }

    public ClubeResponseDTO(String nome, String siglaEstado, LocalDate dataCriacao) {
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.dataCriacao = dataCriacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSiglaEstado() {
        return siglaEstado;
    }

    public void setSiglaEstado(String siglaEstado) {
        this.siglaEstado = siglaEstado;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
