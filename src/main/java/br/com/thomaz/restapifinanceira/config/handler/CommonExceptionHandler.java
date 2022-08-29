package br.com.thomaz.restapifinanceira.config.handler;

import java.time.DateTimeException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import br.com.thomaz.restapifinanceira.config.exception.RegistroNaoEncontradoException;
import br.com.thomaz.restapifinanceira.config.exception.RegistroRepetidoException;
import br.com.thomaz.restapifinanceira.config.exception.UsuarioInexistenteException;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeException.class)
    public ExceptionDto handleDateTimeException() {
        String erro = "Requisição inválida";
        String causa = "Data não existe";
        return new ExceptionDto(erro, causa);
    }
    
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RegistroRepetidoException.class)
    public ExceptionDto handleRegistroRepetidoException(RegistroRepetidoException e) {
        String erro = "Requisição inválida";
        String causa = e.getMessage();
        return new ExceptionDto(erro, causa);
    }
    
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ExceptionDto handleRegistroNotFoundException(RegistroNaoEncontradoException e) {
        String erro = "Recurso não encontrado";
        String causa = e.getMessage();
        return new ExceptionDto(erro, causa);
    }
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsuarioInexistenteException.class)
    public ExceptionDto handleUsuarioInexistenteException(UsuarioInexistenteException e) {
        String erro = "Recurso não encontrado";
        String causa = e.getMessage();
        return new ExceptionDto(erro, causa);
    }

}
