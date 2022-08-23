package br.com.thomaz.restapifinanceira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thomaz.restapifinanceira.controller.helper.ResumoControllerHelper;
import br.com.thomaz.restapifinanceira.dto.ResumoMesDto;
import br.com.thomaz.restapifinanceira.model.Periodo;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    private ResumoControllerHelper helper;
    private ReceitaRepository receitaRepository;
    private DespesaRepository despesaRepository;

    @Autowired
    public ResumoController(ResumoControllerHelper helper, ReceitaRepository recRepo, DespesaRepository desRepo) {
        this.helper = helper;
        this.receitaRepository = recRepo;
        this.despesaRepository = desRepo;
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<ResumoMesDto> resumoMes(@PathVariable int ano, @PathVariable("mes") int mesInt) {
        Periodo mes = Periodo.doMes(mesInt, ano);
        var receitasDoMes = receitaRepository.findByDataBetween(mes.ini(), mes.fim());
        var despesasDoMes = despesaRepository.findByDataBetween(mes.ini(), mes.fim());
        return helper.gerarResumo(receitasDoMes, despesasDoMes);
    }

}
