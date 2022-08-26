package br.com.thomaz.restapifinanceira.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.util.Streamable;
import br.com.thomaz.restapifinanceira.model.Receita;

public interface ReceitaRepository extends MongoRepository<Receita, String>, RegistroRepository {
    
    
    List<Receita> findByUserIdAndDataBetween(String userId,LocalDate ini, LocalDate fim);

    Page<Receita> findByUserIdAndDataBetween(String userId, LocalDate inicio, LocalDate fim, Pageable page);
    
    Page<Receita> findByUserIdAndDescricaoIgnoreCase(String userId, String descricao, Pageable pageable);
    
    @Override
    boolean existsByUserIdAndDataBetweenAndDescricaoIgnoreCaseAndIdNot(String userId, LocalDate inicio, LocalDate fim,
                                                                    String descricao, String id);

    Page<Receita> findByUserId(String userId, Pageable page);

    boolean existsByUserIdAndId(String userId, String id);

}
