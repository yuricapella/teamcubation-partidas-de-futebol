package br.com.meli.teamcubation_partidas_de_futebol.clube.exception;

import br.com.meli.teamcubation_partidas_de_futebol.clube.controller.ClubeApiController;
import br.com.meli.teamcubation_partidas_de_futebol.exception.ErroCodigo;
import br.com.meli.teamcubation_partidas_de_futebol.exception.ErroPadrao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice(assignableTypes = ClubeApiController.class)
public class ClubeApiExceptionHandler {

    @ExceptionHandler({EstadoInexistenteException.class})
    public ResponseEntity<ErroPadrao> handlerEstadoInexistenteException(EstadoInexistenteException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ErroCodigo.ESTADO_INEXISTENTE.name());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erroPadrao);
    }

    @ExceptionHandler({ClubeComNomeJaCadastradoNoEstadoException.class})
    public ResponseEntity<ErroPadrao> handlerClubeComNomeJaCadastradoNoEstadoException(ClubeComNomeJaCadastradoNoEstadoException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ErroCodigo.CLUBE_DUPLICADO.name());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erroPadrao);
    }
}
