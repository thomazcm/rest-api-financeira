package br.com.thomaz.restapifinanceira.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;

public class Registro {

    @Id
    private String id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;

    public Registro(String descricao, BigDecimal valor, LocalDate data) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public int getAno() {
        return data.getYear();
    }

    public int getMes() {
        return data.getMonthValue();
    }

    public Integer getDia() {
        return data.getDayOfMonth();
    }

}
