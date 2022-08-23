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

import br.com.thomaz.restapifinanceira.controller.helper.RegistroControllerHelper;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.model.Periodo;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {
    private ReceitaRepository repository;
    private RegistroControllerHelper  helper;
    
    @Autowired
    public ReceitaController(ReceitaRepository repository, RegistroControllerHelper helper) {
        this.repository = repository;
        this.helper = helper;
    }

    @PostMapping
    public ResponseEntity<ReceitaDto> cadastrar(@Valid @RequestBody RegistroForm form){
        var receita = (Receita) repository.verificaSeAceita(form.toReceita());
        return helper.created(repository.save(receita));
    }

    @GetMapping
    public ResponseEntity<List<ReceitaDto>> listar(String descricao) {
        if (descricao == null) {
            return helper.listarReceitas(repository.findAll());
        }
        return helper.listarReceitas(repository.findByDescricaoIgnoreCase(descricao));
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<List<ReceitaDto>> listarPorMes(@PathVariable int ano, @PathVariable("mes") int mesInt) {
        var mes = Periodo.doMes(mesInt, ano);
        return helper.listarReceitas(repository.findByDataBetween(mes.ini(), mes.fim()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDto> detalhar(@PathVariable String id) {
        if(repository.existsById(id)) {
            return ResponseEntity.ok(new ReceitaDto(repository.findById(id).get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaDto> atualizar(@PathVariable String id,
            @Valid @RequestBody RegistroForm form) {
        if(repository.existsById(id)) {
            var registro = helper.atualizarValores(repository.findById(id).get(), form);
            var receita = (Receita) repository.verificaSeAceita(registro);
            return ResponseEntity.ok(new ReceitaDto(repository.save(receita)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable String id) {
        return helper.delete(id, repository);
    }

}
