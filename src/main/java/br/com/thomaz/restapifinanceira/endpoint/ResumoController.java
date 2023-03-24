package br.com.thomaz.restapifinanceira.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thomaz.restapifinanceira.config.service.TokenService;
import br.com.thomaz.restapifinanceira.endpoint.dto.ResumoMesDto;
import br.com.thomaz.restapifinanceira.endpoint.service.ResumoService;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    private ResumoService helper;
    private UsuarioRepository repo;
    private TokenService tokenService;

    public ResumoController(ResumoService helper, UsuarioRepository repo,
            TokenService tokenService) {
        this.helper = helper;
        this.repo = repo;
        this.tokenService = tokenService;
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<ResumoMesDto> resumoMes(@PathVariable int ano, @PathVariable("mes") int mes,
            @RequestHeader(name = "Authorization") String token) {
        var registros = tokenService.usuarioFromToken(token, repo).getRegistros();
        var receitasDoMes = registros.buscarReceita(ano, mes);
        var despesasDoMes = registros.buscarDespesa(ano, mes);
        return helper.gerarResumo(receitasDoMes, despesasDoMes);
    }

}
