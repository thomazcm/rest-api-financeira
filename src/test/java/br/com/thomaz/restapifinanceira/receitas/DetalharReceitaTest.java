package br.com.thomaz.restapifinanceira.receitas;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.http.ResponseEntity;

import br.com.thomaz.restapifinanceira.controller.ReceitaController;
import br.com.thomaz.restapifinanceira.controller.service.RegistroControllerHelper;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;

class DetalharReceitaTest {

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

    @Test
    void naoDeveDetalharReceitaSeNaoExistir() {
        String id = "1";
        
        when(repository.existsById(id)).thenReturn(false);
        ResponseEntity<ReceitaDto> resposta = controller.detalhar(id);
        
        verifica.codigo404(resposta);
        verify(repository, never()).findById(id);
    }
    
    @Test
    void deveDetalharReceitaSeExistir() {
        String id = "1";
        var receitaOptional = Optional.of(Criar.receita());
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(receitaOptional);
        
        ResponseEntity<ReceitaDto> resposta = controller.detalhar(id);
        
        verify(repository, times(1)).findById(id);
        verifica.atributosIguais(receitaOptional.get(), resposta.getBody());
        verifica.codigo200(resposta);
    }
}
