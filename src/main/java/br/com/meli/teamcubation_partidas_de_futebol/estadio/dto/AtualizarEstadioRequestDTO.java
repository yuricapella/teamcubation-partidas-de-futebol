package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AtualizarEstadioRequestDTO {
    @NotBlank(message = "O nome não pode estar vazio.")
    @Size(min = 3, message = "O nome tem que ter no minimo três letras;")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "O nome deve conter apenas letras sem acento e espaços")
    private String nome;

    public AtualizarEstadioRequestDTO() {
    }

    public AtualizarEstadioRequestDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
