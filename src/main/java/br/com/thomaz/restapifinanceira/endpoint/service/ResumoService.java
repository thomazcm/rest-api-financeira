package br.com.thomaz.restapifinanceira.endpoint.service;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import br.com.thomaz.restapifinanceira.endpoint.dto.ResumoMesDto;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;

@Service
public class ResumoService {

    public ResponseEntity<ResumoMesDto> gerarResumo(List<Receita> receitasDoMes,
            List<Despesa> despesasDoMes) {

        var totalReceitas = receitasDoMes.stream()
                .map(r -> r.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
        var totalDespesas = despesasDoMes.stream()
                .map(r -> r.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
        var gastosPorCategoria = calcularGastosPorCategoria(despesasDoMes);
        return ResponseEntity
                .ok(new ResumoMesDto(totalReceitas, totalDespesas, gastosPorCategoria));
    }

    private EnumMap<CategoriaDespesa, BigDecimal> calcularGastosPorCategoria(
            List<Despesa> despesas) {

        var gastosPorCategoria = new EnumMap<CategoriaDespesa, BigDecimal>(CategoriaDespesa.class);
        despesas.forEach(despesa -> gastosPorCategoria.compute(despesa.getCategoria(),
                (categoria, valorCategoria) -> valorCategoria == null ? despesa.getValor()
                        : valorCategoria.add(despesa.getValor())));
        return gastosPorCategoria;
    }
}
