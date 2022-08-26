package br.com.thomaz.restapifinanceira.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.thomaz.restapifinanceira.model.Despesa;

public interface DespesaRepository extends MongoRepository<Despesa, String>, RegistroRepository{
    
    List<Despesa> findByUserIdAndDataBetween(String userId,LocalDate ini, LocalDate fim);

    Page<Despesa> findByUserIdAndDataBetween(String userId, LocalDate inicio, LocalDate fim, Pageable page);
    
    Page<Despesa> findByUserIdAndDescricaoIgnoreCase(String userId, String descricao, Pageable pageable);
    
    @Override
    boolean existsByUserIdAndDataBetweenAndDescricaoIgnoreCaseAndIdNot(String userId, LocalDate inicio, LocalDate fim,
                                                                    String descricao, String id);

    Page<Despesa> findByUserId(String userId, Pageable page);

    boolean existsByUserIdAndId(String userId, String id);

}

