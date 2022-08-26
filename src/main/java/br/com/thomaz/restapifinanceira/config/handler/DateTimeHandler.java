package br.com.thomaz.restapifinanceira.config.handler;

import java.time.DateTimeException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DateTimeHandler {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeException.class)
    public ExceptionDto handle() {
        String erro = "Requisição inválida";
        String causa = "Data não existe";
        return new ExceptionDto(erro, causa);
    }

}
