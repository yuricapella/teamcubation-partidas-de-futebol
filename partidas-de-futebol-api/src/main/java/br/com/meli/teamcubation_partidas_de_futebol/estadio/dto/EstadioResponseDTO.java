package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto;

public class EstadioResponseDTO {
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