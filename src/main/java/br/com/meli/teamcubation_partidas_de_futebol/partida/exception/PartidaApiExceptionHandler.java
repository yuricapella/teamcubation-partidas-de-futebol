package br.com.meli.teamcubation_partidas_de_futebol.partida.exception;

import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroCodigo;
import br.com.meli.teamcubation_partidas_de_futebol.global_exception.ErroPadrao;
import br.com.meli.teamcubation_partidas_de_futebol.partida.controller.PartidaApiController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = PartidaApiController.class)
public class PartidaApiExceptionHandler {

    @ExceptionHandler({ClubesIguaisException.class})
    public ResponseEntity<ErroPadrao> handlerClubesIguaisException(ClubesIguaisException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ErroCodigo.CLUBES_IGUAIS.name());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erroPadrao);
    }

    @ExceptionHandler({DataPartidaAnteriorACriacaoDoClubeException.class})
    public ResponseEntity<ErroPadrao> handlerDataPartidaAnteriorACriacaoDoClubeException(DataPartidaAnteriorACriacaoDoClubeException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ErroCodigo.DATA_PARTIDA_ANTERIOR_A_CRIACAO_DO_CLUBE.name());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erroPadrao);
    }

    @ExceptionHandler({ClubeInativoException.class})
    public ResponseEntity<ErroPadrao> handlerClubeInativoException(ClubeInativoException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ErroCodigo.CLUBE_INATIVO.name());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erroPadrao);
    }

    @ExceptionHandler({ClubesComPartidasEmHorarioMenorQue48HorasException.class})
    public ResponseEntity<ErroPadrao> handlerClubesComPartidasEmHorarioMenorQue48HorasException(ClubesComPartidasEmHorarioMenorQue48HorasException ex) {
        ErroPadrao erroPadrao = new ErroPadrao();
        erroPadrao.setCodigoErro(ErroCodigo.CLUBE_TEM_PARTIDAS_COM_DATA_MENOR_QUE_48_HORAS_DA_NOVA_PARTIDA.name());
        erroPadrao.setDataHora(LocalDateTime.now());
        erroPadrao.setMensagem(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erroPadrao);
    }

}
