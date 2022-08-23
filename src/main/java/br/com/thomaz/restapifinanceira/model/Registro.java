package br.com.thomaz.restapifinanceira.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Registro {
    
    String getId();
    String getDescricao();
    BigDecimal getValor();
    LocalDate getData();
    
    void setDescricao(String descricao);
    void setData(LocalDate gerarData);
    void setValor(BigDecimal bigDecimal);
    
    default int getDia() {
        return getData().getDayOfMonth();
    }
    
    default int getMes() {
        return getData().getMonthValue();
    }
    
    default int getAno() {
        return getData().getYear();
    }
    
    default CategoriaDespesa getCategoria() {
        try {
            var despesa = (Despesa) this;
            return despesa.getCategoria();
        } catch (ClassCastException e) {
            return null;
        }
    }
    
    default void setCategoria(CategoriaDespesa categoria) {
        try {
            var despesa = (Despesa) this;
            despesa.setCategoria(categoria);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
