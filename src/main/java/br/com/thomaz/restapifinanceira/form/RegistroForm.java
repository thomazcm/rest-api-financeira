package br.com.thomaz.restapifinanceira.form;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.beans.factory.BeanCreationException;

import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;

public class RegistroForm {
    @NotBlank
    private String descricao;
    @NotNull
    @PositiveOrZero
    private Double valor;
    @NotNull
    @Positive
    @Max(31)
    private Integer dia;
    @NotNull
    @Positive
    @Max(12)
    private Integer mes;
    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer ano;
    private String categoria;

    public Receita toReceita() {
        return new Receita(descricao, new BigDecimal(valor), getData());
    }

    public Despesa toDespesa() {
        return new Despesa(descricao, new BigDecimal(valor), getData(), CategoriaDespesa.definir(categoria, CategoriaDespesa.OUTRAS));
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
        CategoriaDespesa.definir(categoria, CategoriaDespesa.OUTRAS);
    }

    public LocalDate getData() {
        try {
            return LocalDate.of(getAno(), getMes(), getDia());
        } catch (DateTimeException e) {
            throw new BeanCreationException("Erro ao salvar registro, data inválida");
        }
    }

}
