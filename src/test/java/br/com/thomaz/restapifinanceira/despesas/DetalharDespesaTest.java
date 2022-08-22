package br.com.thomaz.restapifinanceira.despesas;

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

import br.com.thomaz.restapifinanceira.controller.DespesaController;
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.service.DespesaService;

class DetalharDespesaTest {

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
    void naoDeveDetalharDespesaSeNaoExistir() {
        String id = "1";
        
        when(repository.existsById(id)).thenReturn(false);
        ResponseEntity<DespesaDto> resposta = controller.detalhar(id);
        
        verifica.codigo404(resposta);
        verify(repository, never()).findById(id);
    }
    
    @Test
    void deveDetalharDespesaSeExistir() {
        String id = "1";
        var despesaOptional = Optional.of(Criar.despesa());
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(despesaOptional);
        
        ResponseEntity<DespesaDto> resposta = controller.detalhar(id);
        
        verify(repository, times(1)).findById(id);
        verifica.atributosIguais(despesaOptional.get(), resposta.getBody());
        verifica.codigo200(resposta);
    }
}
