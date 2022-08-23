package br.com.thomaz.restapifinanceira.dto;

import br.com.thomaz.restapifinanceira.model.Registro;

public class ReceitaDto {

    private String id;
    private String descricao;
    private Double valor;
    private int dia;
    private int mes;
    private int ano;

    public ReceitaDto(Registro registro) {
        this.id = registro.getId();
        this.descricao = registro.getDescricao();
        this.valor = registro.getValor().doubleValue();
        this.dia = registro.getDia();
        this.mes = registro.getMes();
        this.ano = registro.getAno();
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

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }
}
