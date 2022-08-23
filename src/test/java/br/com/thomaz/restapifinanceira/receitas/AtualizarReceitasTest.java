package br.com.thomaz.restapifinanceira.receitas;

import static org.junit.jupiter.api.Assertions.fail;
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
import br.com.thomaz.restapifinanceira.controller.helper.RegistroControllerHelper;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;

class AtualizarReceitasTest {
    private MockitoSession session;
    private TesteHelper verifica = new TesteHelper();
    private ReceitaController controller;

    @Mock private ReceitaRepository repository;
    

    @BeforeEach
    void setUp() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        controller = new ReceitaController(repository, new RegistroControllerHelper());
    }

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }
    
    @Test
    void naoDeveAtualizarSeNaoExiste(){
        String id = "1";
        RegistroForm receitaForm = Criar.receitaForm();
        when(repository.existsById(id)).thenReturn(false);
        
        ResponseEntity<ReceitaDto> resposta = controller.atualizar(id, receitaForm);
        
        verify(repository, never()).findById(id);
        verify(repository, never()).verificaSeAceita(Mockito.any());
        verify(repository, never()).save(Mockito.any());
        verifica.codigo404(resposta);
    }
    
    @Test
    void naoDeveAtualizarSeConflitarMesEDescricao() {
        String id = "1";
        var receitaForm = Criar.receitaForm();
        Optional<Receita> receitaOptional = Optional.of(Criar.receita());
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(receitaOptional);
        var receita = receitaOptional.get();
        when(repository.verificaSeAceita(Mockito.any())).thenThrow(IllegalArgumentException.class);
        
        try {
            controller.atualizar(id, receitaForm);
            fail();
        } catch (IllegalArgumentException e) {
            verify(repository, times(1)).findById(id);
            verify(repository, times(1)).verificaSeAceita(receita);
            verify(repository, never()).save(Mockito.any());
        }
    }
    
    @Test
    void deveAtualizarSeTudoEstiverOk() {
        String id = "1";
        var receitaForm = Criar.receitaForm();
        var receitaValoresAtualizados = receitaForm.toReceita();
        var receita = Criar.receita();
        var receitaOptional = Optional.of(receita);
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(receitaOptional);
        when(repository.verificaSeAceita(Mockito.any())).thenReturn(receita);
        when(repository.save(Mockito.any())).thenReturn(receita);
        
        ResponseEntity<ReceitaDto> resposta = controller.atualizar(id, receitaForm);
        
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).verificaSeAceita(receita);
        verify(repository, times(1)).save(receita);
        verifica.codigo200(resposta);
        verifica.atributosIguais(receitaValoresAtualizados, resposta.getBody());
    }
}

