package br.com.thomaz.restapifinanceira.despesas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.http.ResponseEntity;

import br.com.thomaz.restapifinanceira.controller.DespesaController;
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Periodo;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.service.DespesaService;

class ListarDespesasTest {

    @Mock
    private DespesaRepository repository;
    private MockitoSession session;
    private DespesaController controller;
    private TesteHelper verifica = new TesteHelper();

    @BeforeEach
    void setUp() throws Exception {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        controller = new DespesaController(repository, new DespesaService());
    }

    @AfterEach
    void tearDown() throws Exception {
        session.finishMocking();
    }

    @Test
    void deveListarTodasDespesasSeDescricaoNula() {
        when(repository.findAll()).thenReturn(Criar.despesas());
        
        List<DespesaDto> despesas = controller.listar(null);
        
        verify(repository, times(1)).findAll();
        assertEquals(5, despesas.size());
    }

    @Test
    void deveTentarListarPorDescricaoSeExistir() {
        String descricao = "Aluguel";
        List<Despesa> despesas = Criar.despesas();
        Mockito.when(repository.findByDescricaoIgnoreCase(Mockito.anyString())).thenReturn(filtrar(despesas, descricao));
        
        List<DespesaDto> despesasFiltradas = controller.listar(descricao);
        verify(repository, times(1)).findByDescricaoIgnoreCase(descricao);
        assertEquals(3, despesasFiltradas.size());
        assertTrue(despesasFiltradas.stream().allMatch(r -> r.getDescricao().equals(descricao)));
    }
    
    @Test
    void deveListarDespesasPorMes() {
        var despesas = Criar.despesas();
        List<Despesa> despesasFiltradas = filtrar(despesas, Periodo.doMes(1, 2022));
        when(repository.findByDataBetween(LocalDate.of(2021, 12, 31), LocalDate.of(2022, 2, 1)))
        .thenReturn(despesasFiltradas);
        
        ResponseEntity<List<DespesaDto>> resposta = controller.listarPorMes(2022, 1);
        
        verifica.codigo200(resposta);
        assertEquals(3, resposta.getBody().size());
        verify(repository, times(1)).findByDataBetween(LocalDate.of(2021, 12, 31), LocalDate.of(2022, 2, 1));
    }

    @Test
    void deveRetornarNaoEncontradoSeDataInvalida() {
        ResponseEntity<List<DespesaDto>> resposta = controller.listarPorMes(2022, 13);
        verify(repository, never()).findByDataBetween(any(), any());
        verifica.codigo404(resposta);
    }

    private List<Despesa> filtrar(List<Despesa> despesas, Periodo periodo) {
        return despesas.stream()
        .filter(r -> r.getData().isAfter(periodo.ini()) && r.getData().isBefore(periodo.fim()))
        .collect(Collectors.toList());
    }

    private List<Despesa> filtrar(List<Despesa> despesas, String descricao) {
        return despesas.stream().filter(r -> r.getDescricao().equalsIgnoreCase(descricao))
                .collect(Collectors.toList());
    }
}
