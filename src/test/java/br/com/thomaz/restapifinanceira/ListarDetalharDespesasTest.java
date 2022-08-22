package br.com.thomaz.restapifinanceira;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.thomaz.restapifinanceira.controller.DespesaController;
import br.com.thomaz.restapifinanceira.criador.Listas;
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.service.DespesaService;

class ListarDetalharDespesasTest {

    @Mock private DespesaRepository repository;
    private MockitoSession session;
    private DespesaController controller;

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
    void deveListarTodasDespesas() {
        List<Despesa> despesas = Listas.despesas();
        
        when(repository.findAll()).thenReturn(despesas);
        
        var listaSemDescricao = controller.listar(null);
        var listaDeIds = listaSemDescricao.stream().map(DespesaDto::getId).toList();
        
        verify(repository, times(1)).findAll();
        listaSemDescricao.forEach(d -> {
            listaDeIds.contains(d.getId());
        });
        assertEquals(5, listaSemDescricao.size());
        
    }
    
    @Test
    void deveListarPorDescricao() {
        List<Despesa> despesas = Listas.despesas();
        String descricaoValida = "Aluguel";
        String descricaoInvalida = "alugueu";
        when(repository.findByDescricaoIgnoreCase(descricaoValida)).thenReturn(filtrar(despesas, descricaoValida));
        when(repository.findByDescricaoIgnoreCase(descricaoInvalida)).thenReturn(filtrar(despesas, descricaoInvalida));
        var listaComDescricaoValida  = controller.listar(descricaoValida);
        var listaComDescricaoInvalida  = controller.listar(descricaoInvalida);
        
        verify(repository, times(2)).findByDescricaoIgnoreCase(Mockito.anyString());
        assertEquals(3, listaComDescricaoValida.size());
        assertTrue(listaComDescricaoInvalida.isEmpty());
        listaComDescricaoValida.forEach(r -> {
            assertTrue(r.getDescricao().equals("Aluguel"));
        });
    }

    void deveDetalharUmaDespesaSeIdValido() {
        Despesa despesa = Listas.despesas().get(0);
        
        String idValido = "1";
        String idInvalido = "-1";
        
        when(repository.existsById(idInvalido)).thenReturn(true);
        when(repository.existsById(idInvalido)).thenReturn(false);
        when(repository.findById(idValido)).thenReturn(Optional.of(despesa));
        
        ResponseEntity<DespesaDto> dtoValido = controller.detalhar(idValido);
        ResponseEntity<DespesaDto> dtoIdInvalido = controller.detalhar(idInvalido);
        
        verify(repository, times(2)).existsById(Mockito.anyString());
        verify(repository, times(1)).findById(idValido);
        verify(repository, never()).findById(idInvalido);
        assertEquals(HttpStatus.OK, dtoValido.getStatusCode());
        verificaValores(despesa, dtoValido.getBody());
        assertEquals(HttpStatus.NOT_FOUND, dtoIdInvalido.getStatusCode());
    }
    
    private void verificaValores(Despesa despesa, DespesaDto despesaDto) {
        assertEquals(despesa.getDescricao(), despesaDto.getDescricao());
        assertEquals(despesa.getValor(), new BigDecimal(despesaDto.getValor()));
        assertEquals(despesa.getData(), LocalDate.of(despesaDto.getAno(), despesaDto.getMes(), despesaDto.getDia()));
        assertEquals(despesa.getCategoria(), CategoriaDespesa.definir(despesaDto.getCategoria(), null));
    }
    
    private List<Despesa> filtrar(List<Despesa> despesas, String descricao) {
        return despesas.stream().filter(r -> r.getDescricao().equalsIgnoreCase(descricao)).collect(Collectors.toList());
    }
}

