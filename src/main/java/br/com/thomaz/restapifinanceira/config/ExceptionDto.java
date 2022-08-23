package br.com.thomaz.restapifinanceira.config;

public class ExceptionDto {
    private final String erro;
    private final String causa;
    
    public ExceptionDto(String erro, String causa) {
        this.erro = erro;
        this.causa = causa;
    }

    public String getErro() {
        return erro;
    }

    public String getCausa() {
        return causa;
    }
    
}
