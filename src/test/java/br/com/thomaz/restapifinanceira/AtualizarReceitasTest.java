package br.com.thomaz.restapifinanceira;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.thomaz.restapifinanceira.controller.ReceitaController;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;
import br.com.thomaz.restapifinanceira.service.ReceitaService;

class AtualizarReceitasTest {

    @Mock private ReceitaRepository repository;
    private MockitoSession session;
    private ReceitaController controller;
    private TesteHelper helper = new TesteHelper();

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
    void naoDeveAtualizarSeNaoExiste(){
        String id = "1";
        var receitaForm = Criar.receitaForm();
        when(repository.existsById(id)).thenReturn(false);
        
        ResponseEntity<ReceitaDto> resposta = controller.atualizar(id, receitaForm);
        
        verify(repository, never()).findById(id);
        verify(repository, never()).jaPossui(Mockito.any());
        verify(repository, never()).save(Mockito.any());
        assertTrue(helper.status404(resposta));
    }
    
    @Test
    void naoDeveAtualizarSeConflitarMesEDescricao() {
        String id = "1";
        var receitaForm = Criar.receitaForm();
        Optional<Receita> receitaOptional = Optional.of(Criar.receita());
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(receitaOptional);
        var receita = receitaOptional.get();
        when(repository.jaPossui(receita)).thenReturn(true);
        
        ResponseEntity<ReceitaDto> resposta = controller.atualizar(id, receitaForm);
        
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).jaPossui(receita);
        verify(repository, never()).save(Mockito.any());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
    }
    
    @Test
    void deveAtualizarSeTudoEstiverOk() {
        String id = "1";
        var receitaForm = Criar.receitaForm();
        var receitaValoresAtualizados = receitaForm.toReceita();
        Optional<Receita> receitaOptional = Optional.of(Criar.receita());
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(receitaOptional);
        var receita = receitaOptional.get();
        when(repository.jaPossui(receita)).thenReturn(false);
        
        ResponseEntity<ReceitaDto> resposta = controller.atualizar(id, receitaForm);
        
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).jaPossui(receita);
        verify(repository, times(1)).save(receita);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        helper.verificaValores(receitaValoresAtualizados, resposta.getBody());
    }
    
    
}

