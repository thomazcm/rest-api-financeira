package br.com.thomaz.restapifinanceira.despesas;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.DateTimeException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.http.ResponseEntity;

import br.com.thomaz.restapifinanceira.controller.DespesaController;
import br.com.thomaz.restapifinanceira.controller.helper.RegistroControllerHelper;
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;

class CadastrarDespesasTest {
    private TesteHelper verifica = new TesteHelper();
    private MockitoSession session;
    
    private DespesaController controller;
    @Mock private DespesaRepository repository;
    @Captor private ArgumentCaptor<Despesa> captor;
    
    @BeforeEach
    void setUp() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        controller = new DespesaController(repository, new RegistroControllerHelper());
    }

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }
    
    @Test
    void deveCadastrarDespesaValida() {
        var form = Criar.despesaForm();
        var toDespesa = form.toDespesa();
        
        when(repository.verificaMesmoMesComMesmaDescricao(Mockito.any())).thenReturn(toDespesa);
        when(repository.save(any(Despesa.class))).thenReturn(toDespesa);
        
        ResponseEntity<DespesaDto> resposta = controller.cadastrar(form);
        
        verify(repository, times(1)).save(captor.capture());
        Despesa despesaASalvar = captor.getValue();
        
        verify(repository, times(1)).verificaMesmoMesComMesmaDescricao(any(Despesa.class));
        
        verifica.atributosIguais(form, despesaASalvar);
        verifica.atributosIguais(toDespesa, resposta.getBody());
        verifica.codigo201(resposta);
    }
    
    @Test
    void naoDeveCadastrarDespesaSeJaExiste() {
        var form = Criar.despesaForm();
        when(repository.verificaMesmoMesComMesmaDescricao(Mockito.any())).thenThrow(IllegalArgumentException.class);
        
        try {
            controller.cadastrar(form);
            fail();
        } catch (IllegalArgumentException e) {
            verify(repository, never()).save(Mockito.any(Despesa.class));
        }
    }
    
    @Test
    void deveLancarExcecaoDataInvalida() {
        try {
            var form = Criar.formDataInvalida();
            controller.cadastrar(form);
            fail();
        } catch(DateTimeException e){
            verifyNoInteractions(repository);
        }
    }
   
}

