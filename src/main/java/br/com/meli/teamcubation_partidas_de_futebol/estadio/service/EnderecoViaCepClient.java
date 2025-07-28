package br.com.meli.teamcubation_partidas_de_futebol.estadio.service;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.CepResponseDTO;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.dto.EstadioEnderecoResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EnderecoViaCepClient {
    @Value("${url.viacep}")
    private String urlViaCep;

    public CepResponseDTO buscarEndereco(String cep) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(String.format(urlViaCep, cep), CepResponseDTO.class);
    }

    public EstadioEnderecoResponseDTO criarEstadioEnderecoResponseDTO(String nome, String cep) {
        if (cep == null || cep.isBlank()) {
            return new EstadioEnderecoResponseDTO(nome, new CepResponseDTO());
        }
        CepResponseDTO endereco = buscarEndereco(cep);
        return new EstadioEnderecoResponseDTO(nome,endereco);
    }
}
