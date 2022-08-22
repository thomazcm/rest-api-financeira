package br.com.thomaz.restapifinanceira;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.http.HttpStatus;

import br.com.thomaz.restapifinanceira.controller.ReceitaController;
import br.com.thomaz.restapifinanceira.criador.Criador;
import br.com.thomaz.restapifinanceira.dto.ReceitaDto;
import br.com.thomaz.restapifinanceira.model.Receita;
import br.com.thomaz.restapifinanceira.repository.ReceitaRepository;
import br.com.thomaz.restapifinanceira.service.ReceitaService;

class CadastrarReceitasTest {

    @Mock private ReceitaRepository repository;
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
    void deveAceitarApenasReceitasValidas() {
        var criador = new Criador();
        
        var formValido = criador.receita("Rendimentos", 100, 1, 1, 2022).criaForm();
        var receitaValida = formValido.toReceita();
        var formInvalido = criador.receita("Salário", 2000, 1, 1, 2022).criaForm();
        
        when(repository.jaPossui(Mockito.any())).thenReturn(false);
        var retornoValido = controller.criar(formValido);
        when(repository.jaPossui(Mockito.any())).thenReturn(true);
        var retornoInvalido = controller.criar(formInvalido);
        
        Mockito.verify(repository, times(1)).save(Mockito.any());
        assertEquals(HttpStatus.CREATED, retornoValido.getStatusCode());
        verificaValores(receitaValida, retornoValido.getBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, retornoInvalido.getStatusCode());
    }

    private void verificaValores(Receita receita, ReceitaDto receitaDto) {
        assertEquals(receita.getDescricao(), receitaDto.getDescricao());
        assertEquals(receita.getValor(), new BigDecimal(receitaDto.getValor()));
        assertEquals(receita.getData(), LocalDate.of(receitaDto.getAno(), receitaDto.getMes(), receitaDto.getDia()));
    }
}

