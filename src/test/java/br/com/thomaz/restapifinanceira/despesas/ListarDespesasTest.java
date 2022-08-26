package br.com.thomaz.restapifinanceira.despesas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import br.com.thomaz.restapifinanceira.controller.DespesaController;
import br.com.thomaz.restapifinanceira.controller.helper.RegistroControllerHelper;
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.model.Periodo;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;

class ListarDespesasTest {

    @Mock
    private DespesaRepository repository;
    private MockitoSession session;
    private DespesaController controller;
    private TesteHelper verifica = new TesteHelper();

    @BeforeEach
    void setUp() throws Exception {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        controller = new DespesaController(repository, new RegistroControllerHelper());
    }

    @AfterEach
    void tearDown() throws Exception {
        session.finishMocking();
    }

//    @Test
//    void deveListarTodasDespesasSeDescricaoNula() {
//        when(repository.findAll()).thenReturn(Criar.despesas());
//        
//        Page<DespesaDto> despesas = controller.listar(null, null).getBody();
//        
//        verify(repository, times(1)).findAll();
//        assertEquals(5, despesas.getNumberOfElements());
//    }

//    @Test
//    void deveTentarListarPorDescricaoSeExistir() {
//        String descricao = "Aluguel";
//        List<Despesa> despesas = Criar.despesas();
//        Mockito.when(repository.findByDescricaoIgnoreCase(Mockito.anyString(), Mockito.any())).thenReturn(filtrar(despesas, descricao));
//        
//        Page<DespesaDto> despesasFiltradas = controller.listar(descricao, null).getBody();
//        verify(repository, times(1)).findByDescricaoIgnoreCase(descricao, Mockito.any());
//        assertEquals(3, despesasFiltradas.getNumberOfElements());
//        assertTrue(despesasFiltradas.stream().allMatch(r -> r.getDescricao().equals(descricao)));
//    }
    
//    @Test
//    void deveListarDespesasPorMes() {
//        var despesas = Criar.despesas();
//        List<Despesa> despesasFiltradas = filtrar(despesas, Periodo.doMes(1, 2022));
//        when(repository.findByDataBetween(LocalDate.of(2021, 12, 31), LocalDate.of(2022, 2, 1), Mockito.any()))
//        .thenReturn(despesasFiltradas);
//        
//        ResponseEntity<Page<DespesaDto>> resposta = controller.listarPorMes(2022, 1, null);
//        
//        verifica.codigo200(resposta);
//        assertEquals(3, resposta.getBody().getNumberOfElements());
//        verify(repository, times(1)).findByDataBetween(LocalDate.of(2021, 12, 31), LocalDate.of(2022, 2, 1));
//    }

    @Test
    void deveRetornarNaoEncontradoSeDataInvalida() {
        try {
            controller.listarPorMes(2022, 13, null);
            fail();
        } catch(DateTimeException e) {
            verifyNoInteractions(repository);
        }
    }

//    private List<Despesa> filtrar(List<Despesa> despesas, Periodo periodo) {
//        return despesas.stream()
//        .filter(r -> r.getData().isAfter(periodo.ini()) && r.getData().isBefore(periodo.fim()))
//        .collect(Collectors.toList());
//    }

//    private List<Despesa> filtrar(List<Despesa> despesas, String descricao) {
//         List<Despesa> despesas = despesas.stream().filter(r -> r.getDescricao().equalsIgnoreCase(descricao))
//                .collect(Collectors.toList());
//         
//         new Pageable().
//    }
}
