package br.com.thomaz.restapifinanceira.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;

public class RegistroForm {
    
    @NotBlank private String descricao;
    @NotNull @PositiveOrZero private Double valor;
    @NotNull @Positive @Max(31) private Integer dia;
    @NotNull @Positive @Max(12) private Integer mes;
    @NotNull @Min(1900) @Max(2100) private Integer ano;
    
    private String categoria;

    public CategoriaDespesa gerarCategoria() {
        return CategoriaDespesa.fromString(categoria, CategoriaDespesa.OUTRAS);
    }

    public Receita toReceita(String userId) {
        return new Receita(userId, descricao, new BigDecimal(valor), gerarData());
    }

    public Despesa toDespesa(String userId) {
        return new Despesa(userId, descricao, new BigDecimal(valor), gerarData(), gerarCategoria());
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }
    
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate gerarData() {
        return LocalDate.of(getAno(), getMes(), getDia());
    }

}
