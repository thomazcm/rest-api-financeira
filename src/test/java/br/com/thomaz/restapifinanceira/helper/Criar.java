package br.com.thomaz.restapifinanceira.helper;

import java.util.List;

import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.model.Usuario;

public abstract class Criar {
    
    public static Usuario usuario() {
        Usuario usuario = new Usuario("Thomaz Coelho Machado", "thomazcm@gmail.com", "senha1234");
        Criar.receitas().forEach(usuario.getRegistros()::salvar);
        Criar.despesas().forEach(usuario.getRegistros()::salvar);
        return usuario;
    }
    
    public static List<Receita> receitas(){
        return new Construtor().receita("Salário", 3500, 5, 1, 2022)
                                   .receita("Salário", 3500, 5, 2, 2022)
                                   .receita("Salário", 3500, 5, 3, 2022)
                                   .receita("Rendimentos", 250, 10, 1, 2022)
                                   .receita("Reembolso", 750, 5, 1, 2022)
                                   .criaListaReceitas();
    }

    public static List<Despesa> despesas() {
        return new Construtor().despesa("Aluguel", 1000, 10, 1, 2022, "moradia")
                                .despesa("Aluguel", 1000, 10, 2, 2022, "moradia")
                                .despesa("Aluguel", 1000, 10, 3, 2022, "moradia")
                                .despesa("iFood", 200, 10, 1, 2022, "alimentação")
                                .despesa("Cinema", 40, 10, 1, 2022, "lazer")
                                .criaListaDespesas();
    }

    public static RegistroForm formDataInvalida() {
        RegistroForm form = new Construtor().receita("Rendimentos", 100, 1, 2, 2022).criaForm();
        form.setDia(30);
        return form;
    }

    public static Receita receita() {
        return new Construtor().receita("Salário", 2000, 5, 3, 2022).criaReceita();
    }

    public static Despesa despesa() {
        return new Construtor().despesa("Aluguel", 500, 10, 1, 2022, "moradia").criaDespesa();
    }

    public static RegistroForm receitaForm() {
        return new Construtor().receita("Salário", 1000, 4, 4, 2022).criaForm();
    }

    public static RegistroForm despesaForm() {
        return new Construtor().despesa("Aluguel", 500, 10, 4, 2022, "moradia").criaForm();
    }

}
