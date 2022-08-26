package br.com.thomaz.restapifinanceira.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.thomaz.restapifinanceira.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String>{

    Optional<Usuario> findByEmail(String emial);
    
}
