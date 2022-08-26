package br.com.thomaz.restapifinanceira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;

@RestController
@RequestMapping("/admin")
public class TesteController {
    
    @Autowired
    private ReceitaRepository recRepository;
    @Autowired
    private DespesaRepository desRepository;
    
    @DeleteMapping
    public void deleteAll() {
        recRepository.deleteAll();
        desRepository.deleteAll();
    }

}
