package br.com.thomaz.restapifinanceira.dto;

import java.time.LocalDate;

import br.com.thomaz.restapifinanceira.model.Registro;

public class ReceitaDto {

    private String id;
    private String userId;
    private String descricao;
    private Double valor;
    private LocalDate data;

    public ReceitaDto(Registro registro) {
        this.id = registro.getId();
        this.userId =  registro.getUserId();
        this.descricao = registro.getDescricao();
        this.valor = registro.getValor().doubleValue();
        this.data = registro.getData();
    }
    
    public LocalDate getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDescricao() {
        return descricao;
    }

    public Double getValor() {
        return valor;
    }
    
}
