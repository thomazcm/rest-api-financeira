package br.com.thomaz.restapifinanceira.controller.helper;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.model.Registro;
import br.com.thomaz.restapifinanceira.repository.RegistroRepository;

@Service
public class RegistroControllerHelper {

    public ResponseEntity<ReceitaDto> created(Receita receita) {
        return ResponseEntity.created(uri(receita, "/receitas/{id}")).body(new ReceitaDto(receita));
    }

    public ResponseEntity<DespesaDto> created(Despesa despesa) {
        return ResponseEntity.created(uri(despesa, "/despesas/{id}")).body(new DespesaDto(despesa));
    }

    public ResponseEntity<List<DespesaDto>> listarDespesas(List<Despesa> despesas) {
        return ResponseEntity.ok(despesas.stream().map(DespesaDto::new).toList());
    }

    public ResponseEntity<List<ReceitaDto>> listarReceitas(List<Receita> receitas) {
        return ResponseEntity.ok(receitas.stream().map(ReceitaDto::new).toList());
    }

    public Registro atualizarValores(Registro registro, RegistroForm form) {
        registro.setDescricao(form.getDescricao());
        registro.setValor(new BigDecimal(form.getValor()));
        registro.setData(form.gerarData());
        if (registro.getClass().equals(Despesa.class)) {
            var despesa = (Despesa) registro;
            despesa.setCategoria(
                    CategoriaDespesa.fromString(form.getCategoria(), despesa.getCategoria()));
        }
        return registro;
    }

    public ResponseEntity<?> delete(String userId, String id, RegistroRepository repository) {
        if (repository.existsByUserIdAndId(userId, id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private URI uri(Registro registro, String path) {
        return UriComponentsBuilder.fromPath(path).buildAndExpand(registro.getId()).toUri();
    }

}
