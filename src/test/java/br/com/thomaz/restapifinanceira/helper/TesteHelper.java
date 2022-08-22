package br.com.thomaz.restapifinanceira.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BooleanSupplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;

public class TesteHelper {
    
    public void verificaValores(Receita receita, ReceitaDto receitaDto) {
            assertEquals(receita.getDescricao(), receitaDto.getDescricao());
            assertEquals(receita.getValor(), new BigDecimal(receitaDto.getValor()));
            assertEquals(receita.getData(), LocalDate.of(receitaDto.getAno(), receitaDto.getMes(), receitaDto.getDia()));
    }
    
    public void verificaValores(Despesa despesa, DespesaDto despesaDto) {
        assertEquals(despesa.getDescricao(), despesaDto.getDescricao());
        assertEquals(despesa.getValor(), new BigDecimal(despesaDto.getValor()));
        assertEquals(despesa.getData(), LocalDate.of(despesaDto.getAno(), despesaDto.getMes(), despesaDto.getDia()));
        assertEquals(despesa.getCategoria(), CategoriaDespesa.definir(despesaDto.getCategoria(), null));
    }

    public boolean status404(ResponseEntity<?> resposta) {
        return HttpStatus.NOT_FOUND.equals(resposta.getStatusCode());
    }

    public boolean status200(ResponseEntity<?> resposta) {
        return HttpStatus.OK.equals(resposta.getStatusCode());
    }
    
}
