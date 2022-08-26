package br.com.thomaz.restapifinanceira.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "receitas")
public class Receita extends Registro{

    public Receita(String descricao, BigDecimal valor, LocalDate data) {
        super(descricao, valor, data);
    }
}
