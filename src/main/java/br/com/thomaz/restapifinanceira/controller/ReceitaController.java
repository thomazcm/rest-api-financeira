package br.com.thomaz.restapifinanceira.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;
import br.com.thomaz.restapifinanceira.service.ReceitaService;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {
    private ReceitaRepository repository;
    private ReceitaService service;

    @Autowired
    public ReceitaController(ReceitaRepository repository, ReceitaService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReceitaDto> criar(@Valid @RequestBody RegistroForm form) {
        return service.criar(form.toReceita(), repository);
    }

    @GetMapping
    public List<ReceitaDto> listar(String descricao) {
        if (descricao == null) {
            return service.listar(repository.findAll());
        }
        return service.listar(repository.findByDescricaoIgnoreCase(descricao));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDto> detalhar(@PathVariable String id) {
        return service.detalhar(id, repository);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ReceitaDto> atualizar(@PathVariable String id, @Valid @RequestBody RegistroForm form){
        return service.atualizar(id, repository, form);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable String id){
        return service.remover(id, repository);
    }

}
