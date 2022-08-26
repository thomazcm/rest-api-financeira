package br.com.thomaz.restapifinanceira.config.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class IllegalArgumentHandler {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionDto handle() {
        String erro = "Requisição inválida";
        String causa = "Não é possível salvar uma despesa ou receita no mesmo mês com a mesma descrição de outra existente.";
        return new ExceptionDto(erro, causa);
    }

}
