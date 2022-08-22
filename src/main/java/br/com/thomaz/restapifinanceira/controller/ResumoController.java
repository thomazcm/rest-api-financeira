package br.com.thomaz.restapifinanceira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thomaz.restapifinanceira.dto.ResumoMesDto;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;
import br.com.thomaz.restapifinanceira.service.ResumoService;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    private ResumoService service;
    private ReceitaRepository receitaRepository;
    private DespesaRepository despesaRepository;

    @Autowired
    public ResumoController(ResumoService service, ReceitaRepository recRepo, DespesaRepository desRepo) {
        this.service = service;
        this.receitaRepository = recRepo;
        this.despesaRepository = desRepo;
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<ResumoMesDto> resumoMes(@PathVariable int ano, @PathVariable int mes) {
        return service.gerarResumo(ano, mes, receitaRepository, despesaRepository);
    }

}
