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
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Periodo;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;

@RestController
@RequestMapping("/despesas")
public class DespesaController {
    
    private DespesaRepository repository;
    private RegistroControllerHelper helper;

    @Autowired
    public DespesaController(DespesaRepository repository, RegistroControllerHelper helper) {
        this.repository = repository;
        this.helper = helper;
    }

    @PostMapping
    public ResponseEntity<DespesaDto> cadastrar(@Valid @RequestBody RegistroForm form) {
        var despesa = (Despesa) repository.verificaSeAceita(form.toDespesa());
        return helper.created(repository.save(despesa));
    }

    @GetMapping
    public ResponseEntity<List<DespesaDto>> listar(String descricao) {
        if (descricao == null) {
            return helper.listarDespesas(repository.findAll());
        }
        return helper.listarDespesas(repository.findByDescricaoIgnoreCase(descricao));
    }
    

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<List<DespesaDto>> listarPorMes(@PathVariable int ano, @PathVariable("mes") int mesInt) {
        var mes = Periodo.doMes(mesInt, ano);
        return helper.listarDespesas(repository.findByDataBetween(mes.ini(), mes.fim()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDto> detalhar(@PathVariable String id) {
        if(repository.existsById(id)) {
            return ResponseEntity.ok(new DespesaDto(repository.findById(id).get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDto> atualizar(@PathVariable String id,
            @Valid @RequestBody RegistroForm form) {
        if(repository.existsById(id)) {
            var registro = helper.atualizarValores(repository.findById(id).get(), form);
            var despesa = (Despesa) repository.verificaSeAceita(registro);
            return ResponseEntity.ok(new DespesaDto(repository.save(despesa)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable String id) {
        return helper.delete(id, repository);
    }

}
