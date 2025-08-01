package br.com.meli.partidas_de_futebol_mensageria.global_exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<ErroPadrao> handleResponseStatusException(ResponseStatusException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ex.getStatusCode().toString());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getReason());

        return ResponseEntity
                .status(ex.getStatusCode())
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
}
