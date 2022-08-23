package br.com.thomaz.restapifinanceira.controller.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.thomaz.restapifinanceira.dto.ResumoMesDto;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.model.Registro;

@Service
public class ResumoControllerHelper {

    public ResponseEntity<ResumoMesDto> gerarResumo(List<Receita> receitasDoMes, List<Despesa> despesasDoMes) {
        var totalReceitas = calcularTotal(new ArrayList<Registro>(receitasDoMes));
        var totalDespesas = calcularTotal(new ArrayList<Registro>(despesasDoMes));
        var gastosPorCategoria = calcularGastosPorCategoria(despesasDoMes);
        
        return ResponseEntity.ok(new ResumoMesDto(totalReceitas, totalDespesas, gastosPorCategoria));
    }
    
    private  EnumMap<CategoriaDespesa, BigDecimal> calcularGastosPorCategoria(List<Despesa> despesas) {

        var gastosPorCategoria = new EnumMap<CategoriaDespesa, BigDecimal>(CategoriaDespesa.class);
        despesas.forEach(despesa -> {
            var categoriaDespesa = despesa.getCategoria();
            BigDecimal valorDespesa = despesa.getValor();
            BigDecimal totalCategoria = gastosPorCategoria.get(categoriaDespesa);
            gastosPorCategoria.put
            (categoriaDespesa, totalCategoria == null ? valorDespesa : totalCategoria.add(valorDespesa));
        });

        return gastosPorCategoria;
    }

    private BigDecimal calcularTotal(List<Registro> registrosDoMes) {
        return registrosDoMes.stream().map(r -> r.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
