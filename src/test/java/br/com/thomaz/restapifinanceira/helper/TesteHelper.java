package br.com.thomaz.restapifinanceira.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;

public class TesteHelper {

    public void atributosIguais(Receita receita, ReceitaDto receitaDto) {
        assertEquals(receita.getDescricao(), receitaDto.getDescricao());
        assertEquals(receita.getValor(), new BigDecimal(receitaDto.getValor()));
        assertEquals(receita.getData(), receitaDto.getData());
    }

    public void atributosIguais(Despesa despesa, DespesaDto despesaDto) {
        assertEquals(despesa.getDescricao(), despesaDto.getDescricao());
        assertEquals(despesa.getValor(), new BigDecimal(despesaDto.getValor()));
        assertEquals(despesa.getData(), despesaDto.getData());
        assertEquals(despesa.getCategoria(),
                CategoriaDespesa.fromString(despesaDto.getCategoria(), null));
    }

    public void atributosIguais(Receita receita1, Receita receita2) {
        assertEquals(receita1.getDescricao(), receita2.getDescricao());
        assertEquals(receita1.getValor(), receita2.getValor());
        assertEquals(receita1.getData(), receita2.getData());
    }

    public void atributosIguais(Despesa despesa1, Despesa despesa2) {
        assertEquals(despesa1.getDescricao(), despesa2.getDescricao());
        assertEquals(despesa1.getValor(), despesa2.getValor());
        assertEquals(despesa1.getData(), despesa2.getData());
        assertEquals(despesa1.getCategoria(), despesa2.getCategoria());
    }

    public void codigo200(ResponseEntity<?> resposta) {
        assertEquals(200, resposta.getStatusCodeValue());
    }

    public void codigo201(ResponseEntity<?> resposta) {
        assertEquals(201, resposta.getStatusCodeValue());
    }

    public void codigo404(ResponseEntity<?> resposta) {
        assertEquals(404, resposta.getStatusCodeValue());
    }

}
