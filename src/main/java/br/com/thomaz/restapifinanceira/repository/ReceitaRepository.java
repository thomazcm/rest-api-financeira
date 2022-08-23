package br.com.thomaz.restapifinanceira.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.thomaz.restapifinanceira.model.Receita;

public interface ReceitaRepository extends MongoRepository<Receita, String>, RegistroRepository {

    List<Receita> findByDescricaoIgnoreCase(String descricao);
    
    List<Receita> findByDataBetween(LocalDate inicio, LocalDate fim);
    
    boolean existsByDataBetweenAndDescricaoIgnoreCaseAndIdNot(LocalDate inicio, LocalDate fim,
                                                                    String descricao, String id);
    
}
