package br.com.thomaz.restapifinanceira.endpoint.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import br.com.thomaz.restapifinanceira.endpoint.form.UsuarioForm;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.model.Usuario;

@Service
public class DemoService {

    public Usuario gerarUsuarioDemo(UsuarioForm form) {

        var usuario = form.toUsuario();
        var receitas = gerarReceitas();
        var despesas = gerarDespesas();

        receitas.forEach(usuario.getRegistros()::salvar);
        despesas.forEach(usuario.getRegistros()::salvar);
        
        return usuario;
    }

    private List<Receita> gerarReceitas() {
        var receitas = new ArrayList<Receita>();

        int valorSalario = (new Random().nextInt(30, 55)) * 100;

        criarReceitas(receitas, "Salário", valorSalario, valorSalario + 1, 100, 5);
        criarReceitas(receitas, "Rendimentos", 150, 300, 100, 1);
        criarReceitas(receitas, "Reembolso", 100, 700, 15);
        criarReceitas(receitas, "Venda", 150, 800, 20);
        criarReceitas(receitas, "Pagamento Freela", 300, 700, 30);

        return receitas;
    }
    
    private void criarReceitas(List<Receita> receitas, String descricao, int min, int max, int chance) {
        criarReceitas(receitas, descricao, min, max, chance, 0);
    }

    private void criarReceitas(List<Receita> receitas, String descricao, int min, int max, int chance, int dia) {
        var mes = LocalDate.now().getMonthValue();
        var ano = LocalDate.now().getYear();
        var dataBase = LocalDate.of(ano, mes, 1).plusMonths(1);

        for (int i = 0; i < 18; i++) {
            dataBase = dataBase.minusMonths(1);
            LocalDate data = gerarData(dia, dataBase);

            if (chance > new Random().nextInt(0, 100)) {
                var valor = bigDecimalAleatorioEntre(min, max);
                var receita = new Receita(descricao, valor, data);
                receitas.add(receita);
            }

        }
    }

    private List<Despesa> gerarDespesas() {
        var despesas = new ArrayList<Despesa>();

        int valorAluguel = (new Random().nextInt(80, 150) * 10);
        criarDespesas(despesas, "Aluguel", valorAluguel, valorAluguel + 1, 100, "Moradia", 10);
        int valorCondominio = (new Random().nextInt(30, 70) * 10);
        criarDespesas(despesas, "Condomínio", valorCondominio, valorCondominio+1, 100, "Moradia", 10);
        criarDespesas(despesas, "Água", 70, 120, 100, "Moradia", 10);
        criarDespesas(despesas, "Luz", 100, 250, 100, "Moradia", 10);
        criarDespesas(despesas, "Gás", 100, 120, 100, "Moradia");
        
        criarDespesas(despesas, "Supermercado", 300, 800, 100, "Alimentação");
        criarDespesas(despesas, "Delivery", 50, 300, 100, "Alimentação");
        criarDespesas(despesas, "Padaria", 80, 200, 100, "Alimentação");
        
        criarDespesas(despesas, "Passagem", 80, 200, 25, "Transporte");
        criarDespesas(despesas, "Gasolina", 150, 400, 100, "Transporte");
        criarDespesas(despesas, "Pedágio", 15, 30, 10, "Transporte");
        
        criarDespesas(despesas, "Restaurante", 100, 250, 50, "Lazer");
        criarDespesas(despesas, "Cinema", 30, 60, 60, "Lazer");
        criarDespesas(despesas, "Ingressos Show", 120, 121, 30, "Lazer");
        
        int valorUnimed = (new Random().nextInt(25, 60) * 10);
        criarDespesas(despesas, "Plano de Saúde", valorUnimed, valorUnimed+1, 15, "Saúde", 10);
        criarDespesas(despesas, "Dentista", 400, 800, 15, "Saúde");
        
        criarDespesas(despesas, "Bombeiro", 100, 250, 10, "Imprevistos");
        criarDespesas(despesas, "Eletricista", 100, 250, 10, "Imprevistos");
        criarDespesas(despesas, "Veterinário", 150, 250, 15, "Imprevistos");
        
        criarDespesas(despesas, "Faculdade", 580, 581, 100, "Educação", 10);

        return despesas;
    }
    
    private void criarDespesas(List<Despesa> despesas, 
            String descricao, int valorMin, int valorMax, int chance, String categoria) {
        criarDespesas(despesas, descricao, valorMin, valorMax, chance, categoria, 0);
    }

    private void criarDespesas(List<Despesa> despesas, 
            String descricao, int valorMin, int valorMax, int chance, String categoria, int dia) {

        var mes = LocalDate.now().getMonthValue();
        var ano = LocalDate.now().getYear();
        var dataBase = LocalDate.of(ano, mes, 1).plusMonths(1);

        for (int i = 0; i < 18; i++) {
            dataBase = dataBase.minusMonths(1);
            LocalDate data = gerarData(dia, dataBase);
            
            if (chance > new Random().nextInt(0, 100)) {
                var valor = bigDecimalAleatorioEntre(valorMin, valorMax);
                var despesa = new Despesa(descricao, valor, data,
                        CategoriaDespesa.fromString(categoria, CategoriaDespesa.OUTRAS));
                despesas.add(despesa);
            }

        }
    }

    private LocalDate gerarData(int dia, LocalDate dataBase) {
        if (dia == 0) {
            return dataComDiaAleatorio(dataBase);
        } else {
            return LocalDate.of(dataBase.getYear(), dataBase.getMonthValue(), dia);
        }
    }

    private LocalDate dataComDiaAleatorio(LocalDate dataBase) {
        int ultimoDiaMes = dataBase.plusMonths(1).minusDays(1).getDayOfMonth();
        int dia = new Random().nextInt(1, ultimoDiaMes);
        return LocalDate.of(dataBase.getYear(), dataBase.getMonthValue(), dia);
    }

    private BigDecimal bigDecimalAleatorioEntre(int min, int max) {
        int valor = new Random().nextInt(min, max);
        return new BigDecimal(valor).setScale(2);
    }

}
