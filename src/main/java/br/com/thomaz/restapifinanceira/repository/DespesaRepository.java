package br.com.thomaz.restapifinanceira.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.thomaz.restapifinanceira.model.Despesa;

public interface DespesaRepository extends MongoRepository<Despesa, String>, RegistroRepository{
    
    Page<Despesa> findByDescricaoIgnoreCase(String descricao, Pageable page);
    
    List<Despesa> findByDataBetween(LocalDate inicio, LocalDate fim);
    
    Page<Despesa> findByDataBetween(LocalDate ini, LocalDate fim, Pageable page);

    @Override
    boolean existsByDataBetweenAndDescricaoIgnoreCaseAndIdNot(LocalDate inicio, LocalDate fim,
                                                                    String descricao, String id);

}
