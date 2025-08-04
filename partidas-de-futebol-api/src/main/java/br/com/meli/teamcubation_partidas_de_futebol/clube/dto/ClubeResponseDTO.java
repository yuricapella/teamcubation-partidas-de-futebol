package br.com.meli.teamcubation_partidas_de_futebol.clube.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class ClubeResponseDTO {
    @Schema(description = "Nome oficial do clube", example = "Flamengo")
    private String nome;

    @Schema(description = "Sigla do estado do clube", example = "RJ")
    private String siglaEstado;

    @Schema(description = "Data de criação do clube", example = "2025-05-13", type = "string", format = "date")
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
