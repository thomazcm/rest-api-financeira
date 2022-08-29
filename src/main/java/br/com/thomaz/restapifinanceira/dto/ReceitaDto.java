package br.com.thomaz.restapifinanceira.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.model.Registro;

public class ReceitaDto {

    private Long id;
    private String descricao;
    private Double valor;
    private LocalDate data;

    public ReceitaDto(Registro registro) {
        this.id = registro.getId();
        this.descricao = registro.getDescricao();
        this.valor = registro.getValor().doubleValue();
        this.data = registro.getData();
    }

    public LocalDate getData() {
        return data;
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

    public static List<ReceitaDto> listar(List<Receita> receitas) {
        List<ReceitaDto> lista =
                receitas.stream().map(ReceitaDto::new).collect(Collectors.toList());
        lista.sort((r1, r2) -> {
            return r1.getData().compareTo(r2.getData());
        });
        return lista;
    }

}
