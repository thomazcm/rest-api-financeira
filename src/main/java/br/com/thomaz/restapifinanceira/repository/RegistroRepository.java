package br.com.thomaz.restapifinanceira.repository;

import java.time.LocalDate;

import br.com.thomaz.restapifinanceira.model.Periodo;
import br.com.thomaz.restapifinanceira.model.Registro;

public interface RegistroRepository {

    boolean existsByDataBetweenAndDescricaoIgnoreCaseAndIdNot(LocalDate inicio, LocalDate fim,
                                                                    String descricao, String id);

    boolean existsById(String id);

    default boolean jaPossui(Registro registro) {
        var mes = Periodo.doRegistro(registro);
        return existsByDataBetweenAndDescricaoIgnoreCaseAndIdNot(mes.ini(), mes.fim(),
                registro.getDescricao(), registro.getId());
    }

}
