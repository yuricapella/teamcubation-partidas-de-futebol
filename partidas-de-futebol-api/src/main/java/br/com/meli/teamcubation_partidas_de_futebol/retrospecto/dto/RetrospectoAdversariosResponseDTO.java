package br.com.meli.teamcubation_partidas_de_futebol.retrospecto.dto;

import br.com.meli.teamcubation_partidas_de_futebol.retrospecto.model.RetrospectoAdversario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta com retrospecto de um clube contra todos seus adversários")
public class RetrospectoAdversariosResponseDTO {
    @Schema(description = "Nome do clube analisado", example = "Clube de Exemplo Um")
    private final String nomeClube;

    @Schema(description = "Sigla do estado do clube", example = "SP")
    private final String estadoClube;

    @Schema(
            description = "Lista de retrospectos individuais contra cada adversário, trazendo jogos, vitórias, empates, derrotas, gols feitos e sofridos",
            implementation = RetrospectoAdversario.class
    )
    private final List<RetrospectoAdversario>  retrospectoContraAdversarios;

    public RetrospectoAdversariosResponseDTO(String nomeClube, String estadoClube, List<RetrospectoAdversario> retrospectoContraAdversarios) {
        this.nomeClube = nomeClube;
        this.estadoClube = estadoClube;
        this.retrospectoContraAdversarios = retrospectoContraAdversarios;
    }

    public String getNomeClube() {
        return nomeClube;
    }

    public String getEstadoClube() {
        return estadoClube;
    }

    public List<RetrospectoAdversario> getRetrospectoContraAdversarios() {
        return retrospectoContraAdversarios;
    }
}
