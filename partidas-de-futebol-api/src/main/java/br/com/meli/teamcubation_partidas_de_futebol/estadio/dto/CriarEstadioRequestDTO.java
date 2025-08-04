package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criação de estádio (nome e cep)")
public class CriarEstadioRequestDTO {
    @Schema(
            description = "Nome do estádio. Mínimo 3 letras, apenas letras sem acento e espaços.",
            example = "Arena Central"
    )
    @NotNull
    @Size(min = 3, message = "O nome tem que ter no minimo três letras")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "O nome deve conter apenas letras sem acento e espaços")
    private String nome;

    @Schema(
            description = "CEP do estádio. Exatamente 8 dígitos numéricos.",
            example = "88032005"
    )
    @NotNull
    @Pattern(regexp = "^\\d{8}$", message = "O cep deve conter exatamente 8 dígitos numéricos")
    private String cep;

    public CriarEstadioRequestDTO() {
    }

    public CriarEstadioRequestDTO(String nome, String cep) {
        this.nome = nome;
        this.cep = cep;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
