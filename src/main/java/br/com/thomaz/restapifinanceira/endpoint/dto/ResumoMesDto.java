package br.com.thomaz.restapifinanceira.endpoint.dto;

import java.math.BigDecimal;
import java.util.EnumMap;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;

public class ResumoMesDto {
    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;
    private BigDecimal saldoDoMes;
    private EnumMap<CategoriaDespesa, BigDecimal> gastosPorCategoria;

    public ResumoMesDto(BigDecimal totalReceitas, BigDecimal totalDespesas,
            EnumMap<CategoriaDespesa, BigDecimal> gastosPorCategoria) {

        this.totalReceitas = totalReceitas;
        this.totalDespesas = totalDespesas;
        this.saldoDoMes = totalReceitas.subtract(totalDespesas);
        this.gastosPorCategoria = gastosPorCategoria;
    }

    public BigDecimal getTotalReceitas() {
        return totalReceitas;
    }

    public BigDecimal getTotalDespesas() {
        return totalDespesas;
    }

    public BigDecimal getSaldoDoMes() {
        return saldoDoMes;
    }

    public EnumMap<CategoriaDespesa, BigDecimal> getGastosPorCategoria() {
        return gastosPorCategoria;
    }

}
