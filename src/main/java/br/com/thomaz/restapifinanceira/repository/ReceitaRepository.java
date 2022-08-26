package br.com.thomaz.restapifinanceira.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.thomaz.restapifinanceira.model.Receita;

public interface ReceitaRepository extends MongoRepository<Receita, String>, RegistroRepository {
    
    
    List<Receita> findByDataBetween(LocalDate ini, LocalDate fim);

    Page<Receita> findByDataBetween(LocalDate inicio, LocalDate fim, Pageable page);
    
    Page<Receita> findByDescricaoIgnoreCase(String descricao, Pageable pageable);
    
    @Override
    boolean existsByDataBetweenAndDescricaoIgnoreCaseAndIdNot(LocalDate inicio, LocalDate fim,
                                                                    String descricao, String id);

    
}
