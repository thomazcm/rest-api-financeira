package br.com.thomaz.restapifinanceira;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;

import br.com.thomaz.restapifinanceira.controller.DespesaController;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.service.DespesaService;

class ExcluirDespesasTest {

    @Mock private DespesaRepository repository;
    private TesteHelper helper = new TesteHelper();
    private MockitoSession session;
    private DespesaController controller;

    @BeforeEach
    void setUp() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        controller = new DespesaController(repository, new DespesaService());
    }

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }
    
    @Test
    void naoDeveTentarExcluirSeNaoExiste() {
        String id = "1";
        when(repository.existsById(id)).thenReturn(false);
        
        var resposta = controller.remover(id);
        
        Mockito.verify(repository, never()).deleteById(id);
        assertTrue(helper.status404(resposta));
    }
    
    @Test
    void deveExcluirSeEncontrarId() {
        String id = "1";
        when(repository.existsById(id)).thenReturn(true);
        
        var resposta = controller.remover(id);
        
        verify(repository, times(1)).deleteById(id);
        assertTrue(helper.status200(resposta));
    }
}

