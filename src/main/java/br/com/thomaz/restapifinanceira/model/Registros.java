package br.com.thomaz.restapifinanceira.model;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.ObjectUtils;
import br.com.thomaz.restapifinanceira.config.exception.RegistroNaoEncontradoException;
import br.com.thomaz.restapifinanceira.config.exception.RegistroRepetidoException;
import br.com.thomaz.restapifinanceira.endpoint.form.RegistroForm;

@SuppressWarnings("unchecked")
public class Registros {

    private List<Receita> receitas = new ArrayList<>();
    private List<Despesa> despesas = new ArrayList<>();
    private Long receitasId = 1L;
    private Long despesasId = 1L;

    public List<Receita> getReceitas() {
        return Collections.unmodifiableList(receitas);
    }

    public List<Despesa> getDespesas() {
        return Collections.unmodifiableList(despesas);
    }

    public Receita salvar(Receita receita) {
        receita.setId(receitasId);
        receitasId++;
        verificaRepeticao(receita, receitas());
        receitas.add(receita);
        return receita;
    }

    public Despesa salvar(Despesa despesa) {
        despesa.setId(despesasId);
        despesasId++;
        verificaRepeticao(despesa, despesas());
        despesas.add(despesa);
        return despesa;
    }

    public Receita atualizarReceita(Long id, RegistroForm form) {
        var receita = (Receita) atualizarRegistro(buscar(id, receitas()), form);
        verificaRepeticao(receita, receitas());
        return receita;
    }

    public Despesa atualizarDespesa(Long id, RegistroForm form) {
        var despesa = (Despesa) atualizarRegistro(buscar(id, despesas()), form);
        verificaRepeticao(despesa, despesas());
        despesa.setCategoria(
                CategoriaDespesa.fromString(form.getCategoria(), despesa.getCategoria()));
        return despesa;
    }

    public Receita buscarReceita(Long id) {
        return (Receita) buscar(id, receitas());
    }

    public Despesa buscarDespesa(Long id) {
        return (Despesa) buscar(id, despesas());
    }

    public List<Receita> buscarReceita(String descricao) {
        return (List<Receita>) buscar(descricao, receitas());
    }

    public List<Despesa> buscarDespesa(String descricao) {
        return (List<Despesa>) buscar(descricao, despesas());
    }

    public List<Receita> buscarReceita(int ano, int mes) {
        return (List<Receita>) buscar(ano, mes, receitas());
    }

    public List<Despesa> buscarDespesa(int ano, int mes) {
        return (List<Despesa>) buscar(ano, mes, despesas());
    }

    public boolean deletarReceita(Long id) {
        return receitas.removeIf(r -> r.getId() == id);
    }

    public boolean deletarDespesa(Long id) {
        return despesas.removeIf(d -> d.getId() == id);
    }

    private Registro atualizarRegistro(Registro registro, RegistroForm form) {
        registro.setDescricao(form.getDescricao());
        registro.setData(form.gerarData());
        registro.setValor(new BigDecimal(form.getValor()));
        return registro;
    }

    private void verificaRepeticao(Registro registro, List<Registro> registros) {
        if (registros.stream().anyMatch(
                r -> registro.getMes() == r.getMes()
                        && registro.getAno() == r.getAno()
                        && registro.getDescricao().equalsIgnoreCase(r.getDescricao())
                        && !ObjectUtils.nullSafeEquals(registro.getId(), r.getId()))) {
            throw new RegistroRepetidoException(registro);
        }
    }

    private Registro buscar(Long id, List<Registro> registros) {
        var optional = registros.stream().filter(r -> r.getId() == id).findFirst();
        if (optional.isEmpty()) {
            throw new RegistroNaoEncontradoException("Registro não encontrado com o id " + id);
        }
        return optional.get();
    }

    private List<?> buscar(int ano, int mes, List<Registro> registros) {
        return registros.stream()
                .filter(r -> r.getAno() == ano && r.getMes() == mes)
                .collect(Collectors.toList());
    }

    private List<?> buscar(String descricao, List<Registro> registros) {
        return registros.stream()
                .filter(r -> normalize(r.getDescricao()).equals(normalize(descricao)))
                .collect(Collectors.toList());
    }

    private List<Registro> despesas() {
        return new ArrayList<Registro>(despesas);
    }

    private List<Registro> receitas() {
        return new ArrayList<Registro>(receitas);
    }

    private String normalize(String string) {
        return Normalizer
                .normalize(string, Normalizer.Form.NFKD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}
