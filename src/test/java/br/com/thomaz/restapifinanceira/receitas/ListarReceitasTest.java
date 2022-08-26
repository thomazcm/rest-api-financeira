package br.com.thomaz.restapifinanceira.receitas;

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

import br.com.thomaz.restapifinanceira.controller.ReceitaController;
import br.com.thomaz.restapifinanceira.controller.helper.RegistroControllerHelper;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.model.Periodo;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;

class ListarReceitasTest {

    @Mock
    private ReceitaRepository repository;
    private MockitoSession session;
    private ReceitaController controller;
    private TesteHelper verifica = new TesteHelper();

    @BeforeEach
    void setUp() throws Exception {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        controller = new ReceitaController(repository, new RegistroControllerHelper());
    }

    @AfterEach
    void tearDown() throws Exception {
        session.finishMocking();
    }

//    @Test
//    void deveListarTodasReceitasSeDescricaoNula() {
//        when(repository.findAll()).thenReturn(Criar.receitas());
//        
//        Page<ReceitaDto> receitas = controller.listar(null, null).getBody();
//        
//        verify(repository, times(1)).findAll();
//        assertEquals(5, receitas.getNumberOfElements());
//    }

//    @Test
//    void deveTentarListarPorDescricaoSeExistir() {
//        String descricao = "Aluguel";
//        List<Receita> receitas = Criar.receitas();
//        Mockito.when(repository.findByDescricaoIgnoreCase(Mockito.anyString(), Mockito.any())).thenReturn(filtrar(receitas, descricao));
//        
//        Page<ReceitaDto> receitasFiltradas = controller.listar(descricao, null).getBody();
//        verify(repository, times(1)).findByDescricaoIgnoreCase(descricao, Mockito.any());
//        assertEquals(3, receitasFiltradas.getNumberOfElements());
//        assertTrue(receitasFiltradas.stream().allMatch(r -> r.getDescricao().equals(descricao)));
//    }
    
//    @Test
//    void deveListarReceitasPorMes() {
//        var receitas = Criar.receitas();
//        List<Receita> receitasFiltradas = filtrar(receitas, Periodo.doMes(1, 2022));
//        when(repository.findByDataBetween(LocalDate.of(2021, 12, 31), LocalDate.of(2022, 2, 1), Mockito.any()))
//        .thenReturn(receitasFiltradas);
//        
//        ResponseEntity<Page<ReceitaDto>> resposta = controller.listarPorMes(2022, 1, null);
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

//    private List<Receita> filtrar(List<Receita> receitas, Periodo periodo) {
//        return receitas.stream()
//        .filter(r -> r.getData().isAfter(periodo.ini()) && r.getData().isBefore(periodo.fim()))
//        .collect(Collectors.toList());
//    }

//    private List<Receita> filtrar(List<Receita> receitas, String descricao) {
//         List<Receita> receitas = receitas.stream().filter(r -> r.getDescricao().equalsIgnoreCase(descricao))
//                .collect(Collectors.toList());
//         
//         new Pageable().
//    }
}
