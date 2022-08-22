package br.com.thomaz.restapifinanceira.helper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.model.Registro;

public class Criador {
    private List<Receita> receitas = new ArrayList<>();
    private List<Despesa> despesas = new ArrayList<>();
    static int id = 1;
    
    public Criador receita(String descricao, double valor, int dia, int mes, int ano) {
        var receita = new Receita(descricao, new BigDecimal(valor), LocalDate.of(ano, mes, dia));
        receita.setId(String.valueOf(id));
        id++;
        receitas.add(receita);
        return this;
    }
    
    public Criador despesa(String descricao, double valor, int dia, int mes, int ano, String categoria) {
        var despesa = new Despesa(descricao, new BigDecimal(valor), 
                LocalDate.of(ano, mes, dia), CategoriaDespesa.definir(categoria, CategoriaDespesa.OUTRAS));
        despesa.setId(String.valueOf(id));
        id++;
        despesas.add(despesa);
        return this;
    }

    public List<Receita> criaListaReceitas() {
        return receitas;
    }
    
    public Receita criaReceita() {
        return receitas.get(0);
    }
    
    public Despesa criaDespesa() {
        return despesas.get(0);
    }

    public RegistroForm criaForm() {
        var form = new RegistroForm();
        Registro registro;
        if (receitas.isEmpty()) {
           registro = despesas.get(0);
           System.out.println("===========");
           System.out.println("Descricao "+registro.getDescricao());
           System.out.println("Categoria"+ registro.getCategoria());
           System.out.println(CategoriaDespesa.nome(registro.getCategoria()));
           form.setCategoria(CategoriaDespesa.nome(registro.getCategoria()));
        } else {
            registro = receitas.get(0);
        }
        form.setDescricao(registro.getDescricao());
        form.setValor(registro.getValor().doubleValue());
        form.setAno(registro.getAno());
        form.setMes(registro.getMes());
        form.setDia(registro.getDia());
        return form;
    }

    public List<Despesa> criaListaDespesas() {
        return despesas;
    }
    
}
