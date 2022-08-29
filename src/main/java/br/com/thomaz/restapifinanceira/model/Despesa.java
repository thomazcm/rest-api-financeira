package br.com.thomaz.restapifinanceira.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "despesas")
public class Despesa extends Registro{

    private CategoriaDespesa categoria;
    
    public Despesa(String descricao, BigDecimal valor, LocalDate data, CategoriaDespesa categoria) {
        super(descricao, valor, data);
        this.categoria = categoria;
    }

    public CategoriaDespesa getCategoria() {
        return categoria;
    }
    
    public void setCategoria(CategoriaDespesa categoria) {
        this.categoria = categoria;
    }

}
