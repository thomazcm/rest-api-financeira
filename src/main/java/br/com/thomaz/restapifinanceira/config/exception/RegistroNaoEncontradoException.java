package br.com.thomaz.restapifinanceira.config.exception;

public class RegistroNaoEncontradoException extends RuntimeException {
    private static final long serialVersionUID = 989636016406796345L;

    public RegistroNaoEncontradoException(String message) {
        super(message);
    }

}
