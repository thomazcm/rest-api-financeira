package br.com.thomaz.restapifinanceira.receitas;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.http.ResponseEntity;

import br.com.thomaz.restapifinanceira.controller.ReceitaController;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;
import br.com.thomaz.restapifinanceira.service.ReceitaService;

class CadastrarReceitasTest {

    private TesteHelper verifica = new TesteHelper();
    private MockitoSession session;
    
    private ReceitaController controller;
    @Mock private ReceitaRepository repository;
    @Captor private ArgumentCaptor<Receita> captor;
    
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
    void deveCadastrarReceitaValida() {
        var form = Criar.receitaForm();
        var toReceita = form.toReceita();
        
        when(repository.jaPossui(Mockito.any())).thenReturn(false);
        
        ResponseEntity<ReceitaDto> resposta = controller.criar(form);
        verify(repository, times(1)).jaPossui(captor.capture());
        Receita receitaASalvar = captor.getValue();
        
        verify(repository, times(1)).save(receitaASalvar);
        verifica.atributosIguais(toReceita, receitaASalvar);
        verifica.atributosIguais(toReceita, resposta.getBody());
        verifica.codigo201(resposta);
    }
    
    @Test
    void naoDeveCadastrarReceitaSeJaExiste() {
        var form = Criar.receitaForm();
        when(repository.jaPossui(Mockito.any())).thenReturn(true);
        var resposta = controller.criar(form);
        
        verify(repository, never()).save(Mockito.any());
        verifica.codigo422(resposta);
    }
    
    @Test
    void deveLancarExcecaoDataInvalida() {
        try {
            var form = Criar.formDataInvalida();
            controller.criar(form);
            fail();
        } catch(BeanCreationException e){
        }
    }
}

