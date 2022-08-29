package br.com.thomaz.restapifinanceira.config.exception;

import java.math.BigDecimal;
import java.time.LocalDate;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.model.Registro;

public class RegistroRepetidoException extends RuntimeException {
    private static final long serialVersionUID = -1072912377128654140L;
    private String ano;
    private String mes;
    private String descricao;
    private String tipoRegistro;
    private String message;

    public RegistroRepetidoException(Registro registro) {
        this.ano = String.valueOf(registro.getAno());
        this.mes = String.valueOf(registro.getMes());
        this.descricao = registro.getDescricao();
        this.tipoRegistro = registro.getClass().getSimpleName();

        this.message = String.format("Não é possível cadastrar outra %s com a descrição: [%s] "
                + "no mês [%s/%s]", tipoRegistro.toLowerCase(), descricao, mes, ano);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static void main(String[] args) {
        Receita receita = new Receita("salario", new BigDecimal(1000), LocalDate.of(2022, 1, 10));
        var registroRepetidoException = new RegistroRepetidoException(receita);

        System.out.println(registroRepetidoException.getMessage());

    }
}
