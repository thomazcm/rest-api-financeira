package br.com.thomaz.restapifinanceira.config.handler;


public class FormErrorDto {
    
    private final String field;
    private final String message;

    public FormErrorDto(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}
