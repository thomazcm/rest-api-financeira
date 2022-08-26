package br.com.thomaz.restapifinanceira.dto;

import java.time.LocalDate;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Registro;

public class DespesaDto {

    private String id;
    private String descricao;
    private Double valor;
    private LocalDate data;
    private String categoria;

    public DespesaDto(Registro registro) {
        Despesa despesa = (Despesa) registro;
        this.id = despesa.getId();
        this.descricao = despesa.getDescricao();
        this.valor = despesa.getValor().doubleValue();
        this.data = despesa.getData();
        this.categoria = CategoriaDespesa.toString(despesa.getCategoria());
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Double getValor() {
        return valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDate getData() {
        return data;
    }
}
