package br.com.meli.teamcubation_partidas_de_futebol.estadio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO com informações detalhadas do endereço provenientes do CEP.")
public class CepResponseDTO {
    @Schema(description = "CEP consultado", example = "88032005")
    private String cep;
    @Schema(description = "Logradouro do endereço", example = "Rodovia José Carlos Daux")
    private String logradouro;
    @Schema(description = "Bairro do endereço", example = "Saco Grande")
    private String bairro;
    @Schema(description = "Cidade (localidade)", example = "Florianópolis")
    private String localidade;
    @Schema(description = "UF do endereço", example = "SC")
    private String uf;
    @Schema(description = "Nome do estado", example = "Santa Catarina")
    private String estado;

    public CepResponseDTO() {
        this.cep = "";
        this.logradouro = "";
        this.bairro = "";
        this.localidade = "";
        this.uf = "";
        this.estado = "";
    }

    public CepResponseDTO(String cep, String logradouro, String bairro, String localidade, String uf, String estado) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
