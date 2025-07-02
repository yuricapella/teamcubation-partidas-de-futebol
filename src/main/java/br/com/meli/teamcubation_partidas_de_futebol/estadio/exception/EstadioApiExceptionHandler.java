package br.com.meli.teamcubation_partidas_de_futebol.estadio.exception;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.controller.EstadioApiController;
import br.com.meli.teamcubation_partidas_de_futebol.exception.ErroCodigo;
import br.com.meli.teamcubation_partidas_de_futebol.exception.ErroPadrao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice(assignableTypes = EstadioApiController.class)
public class EstadioApiExceptionHandler {

    @ExceptionHandler({EstadioNaoEncontradoException.class})
    public ResponseEntity<ErroPadrao> handlerEstadioNaoEncontradoException(EstadioNaoEncontradoException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ErroCodigo.ESTADIO_NAO_ENCONTRADO.name());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erroPadrao);
    }
}
