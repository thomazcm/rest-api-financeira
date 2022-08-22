package br.com.thomaz.restapifinanceira;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import br.com.thomaz.restapifinanceira.controller.ReceitaController;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;
import br.com.thomaz.restapifinanceira.service.ReceitaService;

class ListarDetalharReceitasTest {

    @Mock private ReceitaRepository repository;
    private MockitoSession session;
    private ReceitaController controller;
    private TesteHelper helper = new TesteHelper();

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
    void deveListarTodasReceitas() {
        List<Receita> receitas = Criar.receitas();
        
        when(repository.findAll()).thenReturn(receitas);
        
        var listaSemDescricao = controller.listar(null);
        var listaDeIds = listaSemDescricao.stream().map(ReceitaDto::getId).toList();
        
        verify(repository, times(1)).findAll();
        listaSemDescricao.forEach(r -> {
            listaDeIds.contains(r.getId());
        });
        assertEquals(5, listaSemDescricao.size());
        
    }
    
    @Test
    void deveListarPorDescricao() {
        List<Receita> receitas = Criar.receitas();
        String descricaoValida = "Salário";
        String descricaoInvalida = "salariu";
        when(repository.findByDescricaoIgnoreCase(descricaoValida)).thenReturn(filtrar(receitas, descricaoValida));
        when(repository.findByDescricaoIgnoreCase(descricaoInvalida)).thenReturn(filtrar(receitas, descricaoInvalida));
        var listaComDescricaoValida  = controller.listar(descricaoValida);
        var listaComDescricaoInvalida  = controller.listar(descricaoInvalida);
        
        verify(repository, times(2)).findByDescricaoIgnoreCase(Mockito.anyString());
        assertEquals(3, listaComDescricaoValida.size());
        assertTrue(listaComDescricaoInvalida.isEmpty());
        listaComDescricaoValida.forEach(r -> {
            assertTrue(r.getDescricao().equals("Salário"));
        });
    }

    void deveDetalharUmaReceitaSeIdValido() {
        Receita receita = Criar.receitas().get(0);
        
        String idValido = "1";
        String idInvalido = "-1";
        
        when(repository.existsById(idInvalido)).thenReturn(true);
        when(repository.existsById(idInvalido)).thenReturn(false);
        when(repository.findById(idValido)).thenReturn(Optional.of(receita));
        
        ResponseEntity<ReceitaDto> dtoValido = controller.detalhar(idValido);
        ResponseEntity<ReceitaDto> dtoIdInvalido = controller.detalhar(idInvalido);
        
        verify(repository, times(2)).existsById(Mockito.anyString());
        verify(repository, times(1)).findById(idValido);
        verify(repository, never()).findById(idInvalido);
        assertEquals(HttpStatus.OK, dtoValido.getStatusCode());
        helper.verificaValores(receita, dtoValido.getBody());
        assertEquals(HttpStatus.NOT_FOUND, dtoIdInvalido.getStatusCode());
    }
    
    private List<Receita> filtrar(List<Receita> receitas, String descricao) {
        return receitas.stream().filter(r -> r.getDescricao().equalsIgnoreCase(descricao)).collect(Collectors.toList());
    }
}

