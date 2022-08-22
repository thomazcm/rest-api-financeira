package br.com.thomaz.restapifinanceira.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Registro;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;

@Service
public class DespesaService {
    
    public ResponseEntity<DespesaDto> criar(Despesa despesa, DespesaRepository repository) {
        if (repository.jaPossui(despesa)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        repository.save(despesa);
        return created(despesa, "despesas/{id}");
    }
    
    public List<DespesaDto> listar (List<Despesa> despesas){
        return despesas.stream().map(DespesaDto::new).collect(Collectors.toList());
    }
    
    public ResponseEntity<DespesaDto> detalhar(String id, DespesaRepository repository) {
        if (repository.existsById(id)) {
            return ResponseEntity.ok(new DespesaDto(repository.findById(id).get()));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<DespesaDto> atualizar(String id, DespesaRepository repository, RegistroForm form) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();   
        }
        
        Despesa atualizada = atualizarValores(repository.findById(id).get(), form);
        
        if (repository.jaPossui(atualizada)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        
        repository.save(atualizada);
        return ResponseEntity.ok(new DespesaDto(atualizada));
    }
    
    public ResponseEntity<?> remover(String id, DespesaRepository repository) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    private Despesa atualizarValores(Despesa despesa, RegistroForm form) {
        despesa.setDescricao(form.getDescricao());
        despesa.setData(form.getData());
        despesa.setValor(new BigDecimal(form.getValor()));
        despesa.setCategoria(CategoriaDespesa.definir(form.getCategoria(), despesa.getCategoria()));
        return despesa;
    }

    private ResponseEntity<DespesaDto> created(Registro registro, String path) {
        return ResponseEntity.created(UriComponentsBuilder.fromPath(path).buildAndExpand(registro.getId()).toUri())
                .body(new DespesaDto(registro));
    }
    
}
