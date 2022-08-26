package br.com.thomaz.restapifinanceira.controller;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import br.com.thomaz.restapifinanceira.dto.UsuarioDto;
import br.com.thomaz.restapifinanceira.form.UsuarioForm;
import br.com.thomaz.restapifinanceira.model.Usuario;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

@RestController @RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    
    @PostMapping
    public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid UsuarioForm form){
        
        Usuario usuario = form.toUsuario();
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        repository.save(usuario);
        URI uri = UriComponentsBuilder.fromPath("usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioDto(usuario));
        
    }
}
