package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta simples com apenas o nome do estádio.")
public class EstadioResponseDTO {
    @Schema(description = "Nome do estádio", example = "Estádio do Pacaembu")
    private String nome;

    public EstadioResponseDTO() {
    }

    public EstadioResponseDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}