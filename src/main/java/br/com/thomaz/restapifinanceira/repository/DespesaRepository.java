package br.com.thomaz.restapifinanceira.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.thomaz.restapifinanceira.model.Despesa;

public interface DespesaRepository extends MongoRepository<Despesa, String>, RegistroRepository{
    
    List<Despesa> findByDescricaoIgnoreCase(String descricao);
    
    List<Despesa> findByDataBetween(LocalDate inicio, LocalDate fim);
    
    boolean existsByDataBetweenAndDescricaoIgnoreCaseAndIdNot(LocalDate inicio, LocalDate fim, String descricao, String id);

}
