package br.com.thomaz.restapifinanceira;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import br.com.thomaz.restapifinanceira.config.service.TokenService;
import br.com.thomaz.restapifinanceira.endpoint.ResumoController;
import br.com.thomaz.restapifinanceira.endpoint.service.ResumoService;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.CategoriaDespesa;
import br.com.thomaz.restapifinanceira.model.Usuario;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

class ResumosTest {
    private TesteHelper helper;
    private MockitoSession mockito;
    private ResumoController controller;
    private Usuario usuario = Criar.usuario();
    private final String TOKEN = "testToken";
    @Mock private UsuarioRepository repository;
    @Mock private TokenService tokenService;
    
    
    @BeforeEach
    void setUp() throws Exception {
        mockito = Mockito.mockitoSession()
           .initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();
        when(tokenService.usuarioFromToken(TOKEN, repository)).thenReturn(usuario);
        controller = new ResumoController(new ResumoService(), repository, tokenService);
        helper = new TesteHelper();
    }
    
    @Test
    void deveGerarResumoCorretamente(){
        var resposta = controller.resumoMes(2022, 1, TOKEN);
        var resumo = resposta.getBody();
        
        helper.codigo200(resposta);
        assertEquals(new BigDecimal(4500), resumo.getTotalReceitas());
        assertEquals(new BigDecimal(1240), resumo.getTotalDespesas());
        assertEquals(new BigDecimal(3260), resumo.getSaldoDoMes());
        assertEquals(2,resumo.getGastosPorCategoria().size());
        assertEquals(new BigDecimal(240), resumo.getGastosPorCategoria().get(CategoriaDespesa.LAZER));
        assertEquals(new BigDecimal(1000), resumo.getGastosPorCategoria().get(CategoriaDespesa.MORADIA));
        verify(tokenService, times(1)).usuarioFromToken(TOKEN, repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockito.finishMocking();
    }

}
