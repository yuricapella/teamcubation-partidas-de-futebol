package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CriarEstadioRequestDTO {
    @Size(min = 3, message = "O nome tem que ter no minimo três letras")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "O nome deve conter apenas letras sem acento e espaços")
    private String nome;
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
