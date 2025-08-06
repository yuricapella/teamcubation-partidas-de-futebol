package br.com.meli.teamcubation_partidas_de_futebol.clube.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "DTO para criar um novo clube")
public class CriarClubeRequestDTO {
    @Schema(
            description = "Nome do clube. Mínimo 2 letras, apenas letras e espaços.",
            example = "Clube de Exemplo dezoito"
    )
    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, message = "O nome tem que ter no minimo duas letras;")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
    private String nome;

    @Schema(
            description = "Sigla do estado, obrigatório 2 caracteres.",
            example = "AM",
            minLength = 2,
            maxLength = 2
    )
    @NotNull(message = "O estado não pode ser nulo")
    @Size(min = 2, max = 2, message = "A sigla do estado só pode ter 2 letras.")
    private String siglaEstado;


    @Schema(
            description = "Data de criação do clube. Não pode ser futura.",
            example = "2025-05-13",
            type = "string",
            format = "date"
    )
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
