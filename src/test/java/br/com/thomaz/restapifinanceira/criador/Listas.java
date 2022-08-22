package br.com.thomaz.restapifinanceira.criador;

import java.util.List;

import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;

public abstract class Listas {

    public static List<Receita> receitas(){
        return new Criador().receita("Salário", 3500, 5, 1, 2022)
                                   .receita("Salário", 3500, 5, 2, 2022)
                                   .receita("Salário", 3500, 5, 3, 2022)
                                   .receita("Venda playstation", 1500, 10, 2, 2022)
                                   .receita("Reembolso", 750, 5, 1, 2022)
                                   .criaListaReceitas();
    }

    public static List<Despesa> despesas() {
        return new Criador().despesa("Aluguel", 1000, 10, 1, 2022, "moradia")
                                .despesa("Aluguel", 1000, 10, 1, 2022, "moradia")
                                .despesa("Aluguel", 1000, 10, 1, 2022, "moradia")
                                .despesa("iFood", 200, 10, 1, 2022, "alimentação")
                                .despesa("Cinema", 40, 10, 1, 2022, "lazer")
                                .criaListaDespesas();
    }
    
}
