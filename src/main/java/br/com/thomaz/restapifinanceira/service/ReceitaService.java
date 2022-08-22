package br.com.thomaz.restapifinanceira.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.model.Registro;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;

@Service
public class ReceitaService{
    
    public ResponseEntity<ReceitaDto> criar(Receita receita, ReceitaRepository repository) {
        if (repository.jaPossui(receita)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        repository.save(receita);
        return created(receita, "receitas/{id}");
    }
    
    public List<ReceitaDto> listar (List<Receita> receitas){
        return receitas.stream().map(ReceitaDto::new).collect(Collectors.toList());
    }
    
    public ResponseEntity<ReceitaDto> detalhar(String id, ReceitaRepository repository) {
        if (repository.existsById(id)) {
            return ResponseEntity.ok(new ReceitaDto(repository.findById(id).get()));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<ReceitaDto> atualizar(String id, ReceitaRepository repository, RegistroForm form) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();   
        }
        
        Receita atualizada = (Receita) atualizarValores(repository.findById(id).get(), form);
        
        if (repository.jaPossui(atualizada)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        
        repository.save(atualizada);
        return ResponseEntity.ok(new ReceitaDto(atualizada));
    }
    
    public ResponseEntity<?> remover(String id, ReceitaRepository repository) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private Registro atualizarValores(Registro registro, RegistroForm form) {
        registro.setDescricao(form.getDescricao());
        registro.setData(form.getData());
        registro.setValor(new BigDecimal(form.getValor()));
        return registro;
    }

    private ResponseEntity<ReceitaDto> created(Registro registro, String path) {
        return ResponseEntity.created(UriComponentsBuilder.fromPath(path).buildAndExpand(registro.getId()).toUri())
                .body(new ReceitaDto(registro));
    }
    
}
