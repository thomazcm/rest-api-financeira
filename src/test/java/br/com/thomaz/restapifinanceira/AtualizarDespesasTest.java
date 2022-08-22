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

import br.com.thomaz.restapifinanceira.controller.DespesaController;
import br.com.thomaz.restapifinanceira.dto.DespesaDto;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Despesa;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.service.DespesaService;

class AtualizarDespesasTest {
    private MockitoSession session;
    private TesteHelper helper = new TesteHelper();
    private DespesaController controller;

    @Mock private DespesaRepository repository;
    

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
    void naoDeveAtualizarSeNaoExiste(){
        String id = "1";
        RegistroForm despesaForm = Criar.despesaForm();
        when(repository.existsById(id)).thenReturn(false);
        
        ResponseEntity<DespesaDto> resposta = controller.atualizar(id, despesaForm);
        
        verify(repository, never()).findById(id);
        verify(repository, never()).jaPossui(Mockito.any());
        verify(repository, never()).save(Mockito.any());
        assertTrue(helper.status404(resposta));
    }
    
    @Test
    void naoDeveAtualizarSeConflitarMesEDescricao() {
        String id = "1";
        var despesaForm = Criar.despesaForm();
        Optional<Despesa> despesaOptional = Optional.of(Criar.despesa());
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(despesaOptional);
        var despesa = despesaOptional.get();
        when(repository.jaPossui(despesa)).thenReturn(true);
        
        ResponseEntity<DespesaDto> resposta = controller.atualizar(id, despesaForm);
        
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).jaPossui(despesa);
        verify(repository, never()).save(Mockito.any());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.getStatusCode());
    }
    
    @Test
    void deveAtualizarSeTudoEstiverOk() {
        String id = "1";
        var despesaForm = Criar.despesaForm();
        var despesaValoresAtualizados = despesaForm.toDespesa();
        Optional<Despesa> despesaOptional = Optional.of(Criar.despesa());
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(despesaOptional);
        var despesa = despesaOptional.get();
        when(repository.jaPossui(despesa)).thenReturn(false);
        
        ResponseEntity<DespesaDto> resposta = controller.atualizar(id, despesaForm);
        
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).jaPossui(despesa);
        verify(repository, times(1)).save(despesa);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        helper.verificaValores(despesaValoresAtualizados, resposta.getBody());
    }
}

