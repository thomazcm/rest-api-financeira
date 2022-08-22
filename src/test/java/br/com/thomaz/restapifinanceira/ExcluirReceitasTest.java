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

import br.com.thomaz.restapifinanceira.controller.ReceitaController;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;
import br.com.thomaz.restapifinanceira.service.ReceitaService;

class ExcluirReceitasTest {

    @Mock private ReceitaRepository repository;
    private TesteHelper helper = new TesteHelper();
    private MockitoSession session;
    private ReceitaController controller;

    @BeforeEach
    void setUp() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        controller = new ReceitaController(repository, new ReceitaService());
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
        
        verify(repository, never()).deleteById(id);
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

