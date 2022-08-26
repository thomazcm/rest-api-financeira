package br.com.thomaz.restapifinanceira.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import br.com.thomaz.restapifinanceira.config.security.TokenService;
import br.com.thomaz.restapifinanceira.controller.helper.RegistroControllerHelper;
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.model.Periodo;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

@RestController
@RequestMapping("/despesas")
public class DespesaController {
    private DespesaRepository repository;
    private RegistroControllerHelper helper;
    private TokenService tokenService;
    
    @Autowired
    public DespesaController(DespesaRepository repository, RegistroControllerHelper helper,
            TokenService tokenService) {
        this.repository = repository;
        this.helper = helper;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<DespesaDto> cadastrar(@Valid @RequestBody RegistroForm form,
            @RequestHeader(name = "Authorization") String token) {
        String userId = tokenService.getUserIdFrom(token);
        var despesa = (Despesa) repository.verificaMesmoMesComMesmaDescricao(userId, form.toDespesa(userId));
        return helper.created(repository.save(despesa));
    }

    @GetMapping
    public ResponseEntity<Page<DespesaDto>> listar(
            @RequestParam(required = false) String descricao,
            @RequestHeader(name = "Authorization") String token,
            @PageableDefault(sort = "data", direction = Direction.ASC) Pageable page) {
        String userId = tokenService.getUserIdFrom(token);
        
        if (descricao == null) {
            Page<DespesaDto> despesas = repository.findByUserId(userId, page).map(DespesaDto::new);
            return ResponseEntity.ok(despesas);
        }
        Page<DespesaDto> despesas =
                repository.findByUserIdAndDescricaoIgnoreCase(userId, descricao, page).map(DespesaDto::new);
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<Page<DespesaDto>> listarPorMes(@PathVariable int ano,
            @PathVariable("mes") int mesInt, @RequestHeader(name = "Authorization") String token,
            @PageableDefault(sort = "data", direction = Direction.ASC) Pageable page) {
        String userId = tokenService.getUserIdFrom(token);
        var mes = Periodo.doMes(mesInt, ano);
        var despesas = repository.findByUserIdAndDataBetween(userId, mes.ini(), mes.fim(), page).map(DespesaDto::new);
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDto> detalhar(@PathVariable String id) {
        if (repository.existsById(id)) {
            return ResponseEntity.ok(new DespesaDto(repository.findById(id).get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDto> atualizar(@PathVariable String id,
            @Valid @RequestBody RegistroForm form, @RequestHeader(name = "Authorization") String token) {
        String userId = tokenService.getUserIdFrom(token);
        if (repository.existsById(id)) {
            var registro = helper.atualizarValores(repository.findById(id).get(), form);
            var despesa = (Despesa) repository.verificaMesmoMesComMesmaDescricao(userId, registro);
            return ResponseEntity.ok(new DespesaDto(repository.save(despesa)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable String id, @RequestHeader(name = "Authorization") String token) {
        String userId = tokenService.getUserIdFrom(token);
        return helper.delete(userId, id, repository);
    }

}
