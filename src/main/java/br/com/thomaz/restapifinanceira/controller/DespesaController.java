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

import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.service.DespesaService;

@RestController
@RequestMapping("/despesas")
public class DespesaController {
    private DespesaRepository repository;
    private DespesaService service;

    @Autowired
    public DespesaController(DespesaRepository repository, DespesaService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DespesaDto> criar(@Valid @RequestBody RegistroForm form) {
        return service.criar(form.toDespesa(), repository);
    }

    @GetMapping
    public List<DespesaDto> listar(String descricao) {
        if (descricao == null) {
            return service.listar(repository.findAll());
        }
        return service.listar(repository.findByDescricaoIgnoreCase(descricao));
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<List<DespesaDto>> listarPorMes(@PathVariable int ano, @PathVariable int mesInt) {
        return service.listarPorMes(mesInt, ano, repository);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDto> detalhar(@PathVariable String id) {
        return service.detalhar(id, repository);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDto> atualizar(@PathVariable String id,
            @Valid @RequestBody RegistroForm form) {
        return service.atualizar(id, repository, form);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable String id) {
        return service.remover(id, repository);
    }

}
