package br.com.thomaz.restapifinanceira.despesas;

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

import br.com.thomaz.restapifinanceira.controller.DespesaController;
import br.com.thomaz.restapifinanceira.controller.helper.RegistroControllerHelper;
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;

class AtualizarDespesasTest {
    private MockitoSession session;
    private TesteHelper verifica = new TesteHelper();
    private DespesaController controller;

    @Mock private DespesaRepository repository;
    

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
    void naoDeveAtualizarSeNaoExiste(){
        String id = "1";
        RegistroForm despesaForm = Criar.despesaForm();
        when(repository.existsById(id)).thenReturn(false);
        
        ResponseEntity<DespesaDto> resposta = controller.atualizar(id, despesaForm);
        
        verify(repository, never()).findById(id);
        verify(repository, never()).verificaSeAceita(Mockito.any());
        verify(repository, never()).save(Mockito.any());
        verifica.codigo404(resposta);
    }
    
    @Test
    void naoDeveAtualizarSeConflitarMesEDescricao() {
        String id = "1";
        var despesaForm = Criar.despesaForm();
        Optional<Despesa> despesaOptional = Optional.of(Criar.despesa());
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(despesaOptional);
        var despesa = despesaOptional.get();
        when(repository.verificaSeAceita(Mockito.any())).thenThrow(IllegalArgumentException.class);
        
        try {
            controller.atualizar(id, despesaForm);
            fail();
        } catch (IllegalArgumentException e) {
            verify(repository, times(1)).findById(id);
            verify(repository, times(1)).verificaSeAceita(despesa);
            verify(repository, never()).save(Mockito.any());
        }
    }
    
    @Test
    void deveAtualizarSeTudoEstiverOk() {
        String id = "1";
        var despesaForm = Criar.despesaForm();
        var despesaValoresAtualizados = despesaForm.toDespesa();
        var despesa = Criar.despesa();
        var despesaOptional = Optional.of(despesa);
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(despesaOptional);
        when(repository.verificaSeAceita(Mockito.any())).thenReturn(despesa);
        when(repository.save(Mockito.any())).thenReturn(despesa);
        
        ResponseEntity<DespesaDto> resposta = controller.atualizar(id, despesaForm);
        
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).verificaSeAceita(despesa);
        verify(repository, times(1)).save(despesa);
        verifica.codigo200(resposta);
        verifica.atributosIguais(despesaValoresAtualizados, resposta.getBody());
    }
}

