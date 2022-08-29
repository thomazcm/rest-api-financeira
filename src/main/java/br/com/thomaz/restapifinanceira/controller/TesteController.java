package br.com.thomaz.restapifinanceira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

@RestController
@RequestMapping("/admin")
public class TesteController {
    
    @Autowired
    private UsuarioRepository userRepository;
    
    @DeleteMapping
    public void deleteAll() {
        userRepository.deleteAll();
    }

}
