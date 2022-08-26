package br.com.thomaz.restapifinanceira.repository;

import java.time.LocalDate;

import br.com.thomaz.restapifinanceira.model.Periodo;
import br.com.thomaz.restapifinanceira.model.Registro;

public interface RegistroRepository {

    void deleteById(String id);
    boolean existsById(String id);
    boolean existsByUserIdAndId(String userId, String id);
    boolean existsByUserIdAndDataBetweenAndDescricaoIgnoreCaseAndIdNot
                        (String userId, LocalDate inicio, LocalDate fim, String descricao, String id);

    
    default Registro verificaMesmoMesComMesmaDescricao(String userId, Registro registro) {
        var mes = Periodo.doRegistro(registro);
        var descricao = registro.getDescricao();
        var id = registro.getId();
        if (existsByUserIdAndDataBetweenAndDescricaoIgnoreCaseAndIdNot(userId, mes.ini(), mes.fim(), descricao, id)) {
            throw new IllegalArgumentException();
        }
        return registro;
    }
    
    
}
