package br.com.thomaz.restapifinanceira.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;

public class DespesaDto {

    private Long id;
    private String descricao;
    private Double valor;
    private LocalDate data;
    private String categoria;

    public DespesaDto(Despesa despesa) {
        this.id = despesa.getId();
        this.descricao = despesa.getDescricao();
        this.valor = despesa.getValor().doubleValue();
        this.data = despesa.getData();
        this.categoria = CategoriaDespesa.toString(despesa.getCategoria());
    }

    public Long getId() {
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

    public static List<DespesaDto> listar(List<Despesa> despesas) {
        var lista = despesas.stream().map(DespesaDto::new).collect(Collectors.toList());
        lista.sort((r1, r2) -> {
            return r1.getData().compareTo(r2.getData());
        });
        return lista;
    }
}
