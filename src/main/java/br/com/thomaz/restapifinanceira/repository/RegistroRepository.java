package br.com.thomaz.restapifinanceira.repository;

import java.time.LocalDate;

import br.com.thomaz.restapifinanceira.model.Registro;

public interface RegistroRepository {
    
    boolean existsByDataBetweenAndDescricaoIgnoreCaseAndIdNot(LocalDate inicio, LocalDate fim, String descricao, String id);
    
    boolean existsById(String id);
    

    default boolean jaPossui(Registro registro) {
        int ano = registro.getAno();
        int mes = registro.getMes();
        var descricao = registro.getDescricao();
        var id = registro.getId();
        
        LocalDate inicioMes = LocalDate.of(ano, mes, 1).minusDays(1);
        LocalDate fimMes = LocalDate.of(mes == 12 ? ano+1 : ano, mes == 12 ? 1 : mes+1, 1);
        
        return existsByDataBetweenAndDescricaoIgnoreCaseAndIdNot(inicioMes, fimMes, descricao, id);
    }
    
}
