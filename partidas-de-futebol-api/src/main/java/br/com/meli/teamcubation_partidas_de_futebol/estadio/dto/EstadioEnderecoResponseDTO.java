package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta contendo o nome do estádio e os dados completos do endereço baseado no cep.")
public record EstadioEnderecoResponseDTO(
        @Schema(description = "Nome do estádio", example = "Estadio de time atualizado com cep meli")
        String nome,
        @Schema(description = "Dados detalhados do endereço do estádio")
        CepResponseDTO endereco
) {}
