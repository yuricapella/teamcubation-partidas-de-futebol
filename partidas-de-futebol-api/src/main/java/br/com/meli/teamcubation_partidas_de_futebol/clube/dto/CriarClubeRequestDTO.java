package br.com.meli.teamcubation_partidas_de_futebol.clube.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class CriarClubeRequestDTO {
    @NotBlank(message = "O nome não pode estar vazio.")
    @Size(min = 2, message = "O nome tem que ter no minimo duas letras;")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
    private String nome;
    @NotBlank(message = "A sigla do estado não pode estar vazia.")
    @Size(min = 2, max = 2, message = "A sigla do estado só pode ter 2 letras.")
    private String siglaEstado;
    @NotNull(message = "Data de criação obrigatória")
    @PastOrPresent(message = "A data de criação não pode ser futura")
    private LocalDate dataCriacao;

    public CriarClubeRequestDTO() {
    }

    public CriarClubeRequestDTO(String nome, String siglaEstado, LocalDate dataCriacao) {
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
