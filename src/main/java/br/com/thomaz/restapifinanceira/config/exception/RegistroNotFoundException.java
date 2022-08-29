package br.com.thomaz.restapifinanceira.config.exception;

public class RegistroNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 989636016406796345L;

    public RegistroNotFoundException(String message) {
        super(message);
    }

}
