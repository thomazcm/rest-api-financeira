package br.com.thomaz.restapifinanceira.resumo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;

import br.com.thomaz.restapifinanceira.controller.ResumoController;
import br.com.thomaz.restapifinanceira.dto.ResumoMesDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;
import br.com.thomaz.restapifinanceira.service.ResumoService;

class ResumoMesTest {
    private MockitoSession session;
    private TesteHelper verifica = new TesteHelper();
    private ResumoController controller;

    @Mock private DespesaRepository despesaRepo;
    @Mock private ReceitaRepository receitaRepo;

    @BeforeEach
    void setUp() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        controller = new ResumoController(new ResumoService(), receitaRepo, despesaRepo);
    }
    
    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @Test
    void deveGerarResumo() {
        when(receitaRepo.findByDataBetween(any(), any())).thenReturn(Criar.receitas());
        when(despesaRepo.findByDataBetween(any(), any())).thenReturn(Criar.despesas());
        var resposta = controller.resumoMes(2022, 1);
        verifica.codigo200(resposta);
        ResumoMesDto resumoDto = resposta.getBody();
        assertEquals(new BigDecimal(12750), resumoDto.getTotalReceitas());
        assertEquals(new BigDecimal(3240), resumoDto.getTotalDespesas());
        assertEquals(new BigDecimal(9510), resumoDto.getSaldoDoMes());
        assertEquals(3, resumoDto.getGastosPorCategoria().size());
        assertEquals(new BigDecimal(200), resumoDto.getGastosPorCategoria().get(CategoriaDespesa.ALIMENTACAO));
        assertEquals(new BigDecimal(40), resumoDto.getGastosPorCategoria().get(CategoriaDespesa.LAZER));
        assertEquals(new BigDecimal(3000), resumoDto.getGastosPorCategoria().get(CategoriaDespesa.MORADIA));
    }
    
    @Test
    void deveGerarResumoSemDespesas() {
            when(receitaRepo.findByDataBetween(any(), any())).thenReturn(Criar.receitas());
            when(despesaRepo.findByDataBetween(any(), any())).thenReturn(new ArrayList<Despesa>());
            var resposta = controller.resumoMes(2022, 1);
            verifica.codigo200(resposta);
            ResumoMesDto resumoDto = resposta.getBody();
            assertEquals(new BigDecimal(12750), resumoDto.getTotalReceitas());
            assertEquals(new BigDecimal(0), resumoDto.getTotalDespesas());
            assertEquals(new BigDecimal(12750), resumoDto.getSaldoDoMes());
            assertTrue(resumoDto.getGastosPorCategoria().isEmpty());
    }
    
    @Test
    void deveGerarResumoSemReceitas() {
        when(receitaRepo.findByDataBetween(any(), any())).thenReturn(new ArrayList<Receita>());
        when(despesaRepo.findByDataBetween(any(), any())).thenReturn(Criar.despesas());
        var resposta = controller.resumoMes(2022, 1);
        verifica.codigo200(resposta);
        ResumoMesDto resumoDto = resposta.getBody();
        assertEquals(new BigDecimal(0), resumoDto.getTotalReceitas());
        assertEquals(new BigDecimal(3240), resumoDto.getTotalDespesas());
        assertEquals(new BigDecimal(-3240), resumoDto.getSaldoDoMes());
        assertEquals(3, resumoDto.getGastosPorCategoria().size());
        assertEquals(new BigDecimal(200), resumoDto.getGastosPorCategoria().get(CategoriaDespesa.ALIMENTACAO));
        assertEquals(new BigDecimal(40), resumoDto.getGastosPorCategoria().get(CategoriaDespesa.LAZER));
        assertEquals(new BigDecimal(3000), resumoDto.getGastosPorCategoria().get(CategoriaDespesa.MORADIA));
    }
    
    @Test 
    void deveGerarResumoEmMesSemAtividade(){
        when(receitaRepo.findByDataBetween(any(), any())).thenReturn(new ArrayList<Receita>());
        when(despesaRepo.findByDataBetween(any(), any())).thenReturn(new ArrayList<Despesa>());
        var resposta = controller.resumoMes(2022, 1);
        verifica.codigo200(resposta);
        ResumoMesDto resumoDto = resposta.getBody();
        assertEquals(new BigDecimal(0), resumoDto.getTotalReceitas());
        assertEquals(new BigDecimal(0), resumoDto.getTotalDespesas());
        assertEquals(new BigDecimal(0), resumoDto.getSaldoDoMes());
        assertTrue(resumoDto.getGastosPorCategoria().isEmpty());
    }
}

