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
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {
    private TokenService tokenService;

    private UsuarioRepository repo;

    @Autowired
    public ReceitaController(UsuarioRepository repo,
            TokenService tokenService) {
        this.repo = repo;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<ReceitaDto> cadastrar(@Valid @RequestBody RegistroForm form,
            @RequestHeader(name = "Authorization") String token, UriComponentsBuilder builder) {
        var usuario = tokenService.usuarioFromToken(token, repo);
        var receita = usuario.getRegistros().salvar(form.toReceita());
        repo.save(usuario);
        var uri = builder.path("receitas/{id}").buildAndExpand(receita.getId()).toUri();
        return ResponseEntity.created(uri).body(new ReceitaDto(receita));
    }

    @GetMapping
    public ResponseEntity<List<ReceitaDto>> listar(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(required = false) String descricao) {
        var registros = tokenService.usuarioFromToken(token, repo).getRegistros();

        if (descricao == null) {
            var receitas = ReceitaDto.listar(registros.getReceitas());
            return ResponseEntity.ok(receitas);
        }
        var receitas = ReceitaDto.listar(registros.buscarReceita(descricao));
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<List<ReceitaDto>> listarPorMes(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable int ano, @PathVariable int mes) {
        var registros = tokenService.usuarioFromToken(token, repo).getRegistros();

        var receitas = ReceitaDto.listar(registros.buscarReceita(ano, mes));
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDto> detalhar(
            @RequestHeader(name = "Authorization") String token, @PathVariable Long id) {
        var registros = tokenService.usuarioFromToken(token, repo).getRegistros();

        var receita = registros.buscarReceita(id);
        return ResponseEntity.ok(new ReceitaDto(receita));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaDto> atualizar(@RequestHeader(name = "Authorization") String token,
            @Valid @RequestBody RegistroForm form, @PathVariable Long id) {
        var usuario = tokenService.usuarioFromToken(token, repo);

        var receita = usuario.getRegistros().atualizarReceita(id, form);
        repo.save(usuario);
        return ResponseEntity.ok(new ReceitaDto(receita));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@RequestHeader(name = "Authorization") String token,
            @PathVariable Long id) {
        var usuario = tokenService.usuarioFromToken(token, repo);
        if (usuario.getRegistros().deletarReceita(id)) {
            repo.save(usuario);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
