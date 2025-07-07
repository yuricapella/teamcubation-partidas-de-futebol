package br.com.meli.teamcubation_partidas_de_futebol.estadio.exception;

import br.com.meli.teamcubation_partidas_de_futebol.estadio.controller.EstadioApiController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = EstadioApiController.class)
public class EstadioApiExceptionHandler {

}
