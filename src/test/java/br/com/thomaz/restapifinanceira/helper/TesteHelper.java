package br.com.thomaz.restapifinanceira.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;

import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.model.Registro;

public class TesteHelper {
//    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//    
//    public boolean validaForm(RegistroForm form) {
//        var violations = validator.validate(form);
//        System.out.println(violations.size());
//        return violations.isEmpty();
//    }
//    
    public void atributosIguais(Receita receita, ReceitaDto receitaDto) {
            assertEquals(receita.getDescricao(), receitaDto.getDescricao());
            assertEquals(receita.getValor(), new BigDecimal(receitaDto.getValor()));
            assertEquals(receita.getData(), receitaDto.getData());
    }
    
    public void atributosIguais(Despesa despesa, DespesaDto despesaDto) {
        assertEquals(despesa.getDescricao(), despesaDto.getDescricao());
        assertEquals(despesa.getValor(), new BigDecimal(despesaDto.getValor()));
        assertEquals(despesa.getData(), despesaDto.getData());
        assertEquals(despesa.getCategoria(), CategoriaDespesa.fromString(despesaDto.getCategoria(), null));
    }

    public void atributosIguais(Registro registro1, Registro registro2) {
        assertEquals(registro1.getDescricao(), registro2.getDescricao());
        assertEquals(registro1.getValor(), registro2.getValor());
        assertEquals(registro1.getData(), registro2.getData());
    }

    public void atributosIguais(RegistroForm form, Registro registro) {
        assertEquals(form.getDescricao(), registro.getDescricao());
        assertEquals(new BigDecimal(form.getValor()), registro.getValor());
        assertEquals(form.gerarData(), registro.getData());
    }

    public void codigo200(ResponseEntity<?> resposta) {
        assertEquals(200, resposta.getStatusCodeValue());
    }

    public void codigo201(ResponseEntity<?> resposta) {
        assertEquals(201, resposta.getStatusCodeValue());
    }

    public void codigo400(ResponseEntity<?> resposta) {
        assertEquals(400, resposta.getStatusCodeValue());
    }

    public void codigo404(ResponseEntity<?> resposta) {
        assertEquals(404, resposta.getStatusCodeValue());
    }
    
}
