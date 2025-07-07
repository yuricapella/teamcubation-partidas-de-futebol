package br.com.meli.teamcubation_partidas_de_futebol.global_exception;

import br.com.meli.teamcubation_partidas_de_futebol.clube.exception.ClubeNaoEncontradoException;
import br.com.meli.teamcubation_partidas_de_futebol.estadio.exception.EstadioNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalApiExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErroPadrao> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ErroCodigo.CAMPO_INVALIDO.name());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getBody().getDetail());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(erro -> {
            String campo = ((FieldError) erro).getField();
            String mensagemErroCampo = erro.getDefaultMessage();
            errors.put(campo, mensagemErroCampo);
        });
        erroPadrao.setErrors(errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erroPadrao);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroPadrao> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setCodigoErro(ErroCodigo.REQUISICAO_INVALIDA.name());
        erroPadrao.setMensagem("Não foi possível ler o JSON da requisição.");
        erroPadrao.setErrors(null);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erroPadrao);
    }

    @ExceptionHandler({ClubeNaoEncontradoException.class})
    public ResponseEntity<ErroPadrao> handlerClubeNaoEncontradoException(ClubeNaoEncontradoException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ErroCodigo.CLUBE_NAO_ENCONTRADO.name());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erroPadrao);
    }

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
