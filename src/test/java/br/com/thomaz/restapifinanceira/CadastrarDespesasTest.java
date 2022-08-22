package br.com.thomaz.restapifinanceira;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.http.HttpStatus;

import br.com.thomaz.restapifinanceira.controller.DespesaController;
import br.com.thomaz.restapifinanceira.helper.Criador;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.repository.DespesaRepository;
import br.com.thomaz.restapifinanceira.service.DespesaService;

class CadastrarDespesasTest {
    private TesteHelper helper = new TesteHelper();
    @Mock private DespesaRepository repository;
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
    void deveAceitarApenasDespesasValidas() {
        var criador = new Criador();
        
        var formValido = criador.despesa("Juros", 100, 1, 1, 2022, "outras").criaForm();
        var despesaValida = formValido.toDespesa();
        var formInvalido = criador.despesa("Aluguel", 2000, 1, 1, 2022, "moradia").criaForm();
        
        when(repository.jaPossui(Mockito.any())).thenReturn(false);
        var retornoValido = controller.criar(formValido);
        when(repository.jaPossui(Mockito.any())).thenReturn(true);
        var retornoInvalido = controller.criar(formInvalido);
        
        Mockito.verify(repository, times(1)).save(Mockito.any());
        assertEquals(HttpStatus.CREATED, retornoValido.getStatusCode());
        helper.verificaValores(despesaValida, retornoValido.getBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, retornoInvalido.getStatusCode());
    }
   
}

