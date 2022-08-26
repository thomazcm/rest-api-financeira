package br.com.thomaz.restapifinanceira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.thomaz.restapifinanceira.config.security.TokenService;
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
    private TokenService tokenService;

    @Autowired
    public ResumoController(ResumoControllerHelper helper, ReceitaRepository recRepo, DespesaRepository desRepo,
            TokenService tokenService) {
        this.helper = helper;
        this.receitaRepository = recRepo;
        this.despesaRepository = desRepo;
        this.tokenService = tokenService;
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<ResumoMesDto> resumoMes(@PathVariable int ano, @PathVariable("mes") int mesInt,
            @RequestHeader(name = "Authorization") String token) {
        String userId = tokenService.getUserIdFrom(token);
        Periodo mes = Periodo.doMes(mesInt, ano);
        var receitasDoMes = receitaRepository.findByUserIdAndDataBetween(userId, mes.ini(), mes.fim());
        var despesasDoMes = despesaRepository.findByUserIdAndDataBetween(userId, mes.ini(), mes.fim());
        return helper.gerarResumo(receitasDoMes, despesasDoMes);
    }

}
