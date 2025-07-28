package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto;

public record CepResponseDTO(
    String cep,
    String logradouro,
    String bairro,
    String localidade,
    String uf,
    String estado
) {}
