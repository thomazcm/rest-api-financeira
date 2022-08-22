package br.com.thomaz.restapifinanceira.receitas;

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

import br.com.thomaz.restapifinanceira.controller.ReceitaController;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;
import br.com.thomaz.restapifinanceira.service.Periodo;
import br.com.thomaz.restapifinanceira.service.ReceitaService;

class ListarReceitasTest {

    @Mock
    private ReceitaRepository repository;
    private MockitoSession session;
    private ReceitaController controller;
    private TesteHelper verifica = new TesteHelper();

    @BeforeEach
    void setUp() throws Exception {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        controller = new ReceitaController(repository, new ReceitaService());
    }

    @AfterEach
    void tearDown() throws Exception {
        session.finishMocking();
    }

    @Test
    void deveListarTodasReceitasSeDescricaoNula() {
        when(repository.findAll()).thenReturn(Criar.receitas());
        
        List<ReceitaDto> receitas = controller.listar(null);
        
        verify(repository, times(1)).findAll();
        assertEquals(5, receitas.size());
    }

    @Test
    void deveTentarListarPorDescricaoSeExistir() {
        String descricao = "Salário";
        List<Receita> receitas = Criar.receitas();
        Mockito.when(repository.findByDescricaoIgnoreCase(Mockito.anyString())).thenReturn(filtrar(receitas, descricao));
        
        List<ReceitaDto> receitasFiltradas = controller.listar(descricao);
        verify(repository, times(1)).findByDescricaoIgnoreCase(descricao);
        assertEquals(3, receitasFiltradas.size());
        assertTrue(receitasFiltradas.stream().allMatch(r -> r.getDescricao().equals(descricao)));
    }
    
    @Test
    void deveListarReceitasPorMes() {
        var receitas = Criar.receitas();
        List<Receita> receitasFiltradas = filtrar(receitas, Periodo.doMes(1, 2022));
        when(repository.findByDataBetween(LocalDate.of(2021, 12, 31), LocalDate.of(2022, 2, 1)))
        .thenReturn(receitasFiltradas);
        
        ResponseEntity<List<ReceitaDto>> resposta = controller.listarPorMes(2022, 1);
        
        verifica.codigo200(resposta);
        assertEquals(3, resposta.getBody().size());
        verify(repository, times(1)).findByDataBetween(LocalDate.of(2021, 12, 31), LocalDate.of(2022, 2, 1));
    }

    @Test
    void deveRetornarNaoEncontradoSeDataInvalida() {
        ResponseEntity<List<ReceitaDto>> resposta = controller.listarPorMes(2022, 13);
        verify(repository, never()).findByDataBetween(any(), any());
        verifica.codigo404(resposta);
    }

    private List<Receita> filtrar(List<Receita> receitas, Periodo periodo) {
        return receitas.stream()
        .filter(r -> r.getData().isAfter(periodo.ini()) && r.getData().isBefore(periodo.fim()))
        .collect(Collectors.toList());
    }

    private List<Receita> filtrar(List<Receita> receitas, String descricao) {
        return receitas.stream().filter(r -> r.getDescricao().equalsIgnoreCase(descricao))
                .collect(Collectors.toList());
    }
}
