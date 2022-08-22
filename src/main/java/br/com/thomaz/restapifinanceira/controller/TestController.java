package br.com.thomaz.restapifinanceira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;

@RestController
public class TestController {
    
    @Autowired
    private ReceitaRepository receitaRepository;
    private DespesaRepository despesaRepository;
    
    @DeleteMapping("/receitas/deleteAll")
    public ResponseEntity<?> deleteAllReceitas(){
        receitaRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/despesas/deleteAll")
    public ResponseEntity<?> deleteAllDespesas(){
        despesaRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
    
}
