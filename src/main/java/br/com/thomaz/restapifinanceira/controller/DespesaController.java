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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import br.com.thomaz.restapifinanceira.config.security.TokenService;
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

@RestController
@RequestMapping("/despesas")
public class DespesaController {
    private TokenService tokenService;
    private UsuarioRepository repo;

    @Autowired
    public DespesaController(UsuarioRepository repo,
            TokenService tokenService) {
        this.repo = repo;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<DespesaDto> cadastrar(@Valid @RequestBody RegistroForm form,
            @RequestHeader(name = "Authorization") String token, UriComponentsBuilder builder) {
        var usuario = tokenService.usuarioFromToken(token, repo);
        var despesa = usuario.getRegistros().salvarDespesa(form.toDespesa());
        repo.save(usuario);
        var uri = builder.path("despesas/{id}").buildAndExpand(despesa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DespesaDto(despesa));
    }

    @GetMapping
    public ResponseEntity<List<DespesaDto>> listar(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(required = false) String descricao) {
        var registros = tokenService.usuarioFromToken(token, repo).getRegistros();

        if (descricao == null) {
            var despesas = DespesaDto.listar(registros.getDespesas());
            return ResponseEntity.ok(despesas);
        }
        var despesas = DespesaDto.listar(registros.buscarDespesa(descricao));
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<List<DespesaDto>> listarPorMes(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int ano, @PathVariable int mes) {
        var registros = tokenService.usuarioFromToken(token, repo).getRegistros();

        var despesas = DespesaDto.listar(registros.buscarDespesa(ano, mes));
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDto> detalhar(
            @RequestHeader(name = "Authorization") String token, @PathVariable Long id) {
        var registros = tokenService.usuarioFromToken(token, repo).getRegistros();

        var despesa = registros.buscarDespesa(id);
        return ResponseEntity.ok(new DespesaDto(despesa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDto> atualizar(@RequestHeader(name = "Authorization") String token,
            @Valid @RequestBody RegistroForm form, @PathVariable Long id) {
        var usuario = tokenService.usuarioFromToken(token, repo);
    
        var despesa = usuario.getRegistros().atualizarDespesa(id, form);
        repo.save(usuario);
        return ResponseEntity.ok(new DespesaDto(despesa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@RequestHeader(name = "Authorization") String token,
            @PathVariable Long id) {
        var usuario = tokenService.usuarioFromToken(token, repo);
        if (usuario.getRegistros().deletarDespesa(id)) {
            repo.save(usuario);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
