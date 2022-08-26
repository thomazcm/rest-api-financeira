package br.com.thomaz.restapifinanceira.config.security;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.thomaz.restapifinanceira.model.Usuario;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService{
    
    private UsuarioRepository repository;
    @Autowired
    public AutenticacaoService(UsuarioRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = repository.findByEmail(username);
        if (usuario.isPresent()) {
            return usuario.get();
        }
        throw new UsernameNotFoundException("Dados inválidos!");
    }

}
