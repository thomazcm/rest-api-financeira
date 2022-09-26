package br.com.thomaz.restapifinanceira.config.exception;


public class UsuarioInexistenteException extends RuntimeException {
    
    private static final long serialVersionUID = 6917764455379978819L;

    public UsuarioInexistenteException() {
        super("Não foi possível encontrar o usuário da requisição");
    }

}
