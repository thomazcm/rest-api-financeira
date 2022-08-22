package br.com.thomaz.restapifinanceira.service;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.thomaz.restapifinanceira.dto.ResumoMesDto;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Periodo;
import br.com.thomaz.restapifinanceira.model.Registro;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;

@Service
public class ResumoService {

    public ResponseEntity<ResumoMesDto> gerarResumo(int anoInt, int mesInt, ReceitaRepository receitaRepository,
            DespesaRepository despesaRepository) {
        
        Periodo mes;
        try {
            mes = Periodo.doMes(mesInt, anoInt);
        } catch (DateTimeException e) {
            return ResponseEntity.notFound().build();
        }
        
        var receitasDoMes = receitaRepository.findByDataBetween(mes.ini(), mes.fim());
        var despesasDoMes = despesaRepository.findByDataBetween(mes.ini(), mes.fim());
        var totalReceitas = calcularTotal(new ArrayList<Registro>(receitasDoMes));
        var totalDespesas = calcularTotal(new ArrayList<Registro>(despesasDoMes));
        EnumMap<CategoriaDespesa, BigDecimal> gastosPorCategoria = calcularGastosPorCategoria(despesasDoMes);
        
        return ResponseEntity.ok(new ResumoMesDto(totalReceitas, totalDespesas, gastosPorCategoria));
    }
    
    private  EnumMap<CategoriaDespesa, BigDecimal> calcularGastosPorCategoria(List<Despesa> despesas) {

        var gastosPorCategoria = new EnumMap<CategoriaDespesa, BigDecimal>(CategoriaDespesa.class);
        despesas.forEach(despesa -> {
            var categoriaDespesa = despesa.getCategoria();
            BigDecimal valorDespesa = despesa.getValor();
            BigDecimal totalCategoria = gastosPorCategoria.get(categoriaDespesa);
            
            if (totalCategoria == null) {
                gastosPorCategoria.put(categoriaDespesa, valorDespesa);
            } else {
                gastosPorCategoria.put(categoriaDespesa, totalCategoria.add(valorDespesa));
            }
        });
        
        return gastosPorCategoria;
    }

    private BigDecimal calcularTotal(List<Registro> registrosDoMes) {
        return registrosDoMes.stream().map(r -> r.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
