package br.com.meli.teamcubation_partidas_de_futebol.estadio.exception;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.controller.EstadioApiController;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroCodigo;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = EstadioApiController.class)
public class EstadioApiExceptionHandler {
    @ExceptionHandler({EstadioJaExisteException.class})
    public ResponseEntity<ErroPadrao> handlerEstadioJaExisteException(EstadioJaExisteException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ErroCodigo.ESTADIO_JA_EXISTE.name());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erroPadrao);
    }
}
