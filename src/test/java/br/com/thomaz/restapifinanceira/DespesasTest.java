package br.com.thomaz.restapifinanceira;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.DateTimeException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;
import org.springframework.web.util.UriComponentsBuilder;
import br.com.thomaz.restapifinanceira.config.exception.RegistroRepetidoException;
import br.com.thomaz.restapifinanceira.config.security.TokenService;
import br.com.thomaz.restapifinanceira.controller.DespesaController;
import br.com.thomaz.restapifinanceira.form.RegistroForm;
import br.com.thomaz.restapifinanceira.helper.Criar;
import br.com.thomaz.restapifinanceira.helper.TesteHelper;
import br.com.thomaz.restapifinanceira.model.Usuario;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

class DespesasTest {
    private TesteHelper helper;
    private MockitoSession mockito;
    private DespesaController controller;
    private Usuario usuario = Criar.usuario();
    private final String TOKEN = "testToken";
    @Mock private UsuarioRepository repository;
    @Mock private TokenService tokenService;
    
    
    @Test
    void deveCadastrarFormValido() {
        RegistroForm form = Criar.despesaForm();
        var resposta = controller.cadastrar(form, TOKEN, UriComponentsBuilder.newInstance());
        var despesaDto = resposta.getBody();
        
        helper.codigo201(resposta);
        helper.atributosIguais(form.toDespesa(), despesaDto);
        verify(repository, times(1)).save(usuario);
        verify(tokenService, times(1)).usuarioFromToken(TOKEN, repository);
        assertEquals(6L, despesaDto.getId());
        assertEquals(6, usuario.getRegistros().getDespesas().size());
    }
    
    @Test
    void naoDeveCadastrarDespesaInvalida() {
        RegistroForm form = Criar.despesaForm();
        form.setMes(2);
        try {
            controller.cadastrar(form, TOKEN, UriComponentsBuilder.newInstance());
            fail();
        } catch(RegistroRepetidoException e) {
        }
        form.setAno(2021);
        form.setDia(30);
        try {
            controller.cadastrar(form, TOKEN, UriComponentsBuilder.newInstance());
            fail();
        } catch(DateTimeException e) {
        }
      
        verify(repository, times(0)).save(usuario);
        assertEquals(5, usuario.getRegistros().getDespesas().size());
        verify(tokenService, times(2)).usuarioFromToken(TOKEN, repository);
    }

    @Test
    void develistarTodasOuPorDescricaoTest() {
        String descricao = null;
        var resposta = controller.listar(TOKEN, descricao);
        var despesas = resposta.getBody();
        
        helper.codigo200(resposta);
        assertEquals(5, despesas.size());
        
        descricao = "Aluguel";
        resposta = controller.listar(TOKEN, descricao);
        despesas = resposta.getBody();
        
        helper.codigo200(resposta);
        assertEquals(3, despesas.size());
        assertTrue(despesas.stream().allMatch(r->r.getDescricao().equals("Aluguel")));
        
        verify(tokenService, times(2)).usuarioFromToken(TOKEN, repository);
    }
    
    @Test
    void develistarPorMes() {
        var resposta1 = controller.listarPorMes(TOKEN, 2022, 1);
        var resposta2 = controller.listarPorMes(TOKEN, 2022, 2);
        var resposta3 = controller.listarPorMes(TOKEN, 2022, 6);
        
        List.of(resposta1, resposta2, resposta3).forEach(r -> helper.codigo200(r));
        assertEquals(3, resposta1.getBody().size());
        assertEquals(1, resposta2.getBody().size());
        assertEquals(0, resposta3.getBody().size());
        
        verify(tokenService, times(3)).usuarioFromToken(TOKEN, repository);
    }
    
    @Test
    void deveDetalharSeExistir() {
        Long id = 1L;
        var amostra = usuario.getRegistros().getDespesas().get(0);
        
        var resposta = controller.detalhar(TOKEN, id);
        var despesaDto = resposta.getBody();
        helper.codigo200(resposta);
        helper.atributosIguais(amostra, despesaDto);
        
        id = 10L;
        resposta = controller.detalhar(TOKEN, id);
        helper.codigo404(resposta);
        verify(tokenService, times(2)).usuarioFromToken(TOKEN, repository);
    }
    
    @Test
    void deveAtualizarSeExisteAndValida() {
        var form = Criar.despesaForm();
        var amostra = form.toDespesa();
        var resposta = controller.atualizar(TOKEN, form, 4L);
        var despesaDto = resposta.getBody();
        var despesaAtualizada = usuario.getRegistros().getDespesas().get(3);
        
        helper.codigo200(resposta);
        helper.atributosIguais(amostra, despesaDto);
        helper.atributosIguais(amostra, despesaAtualizada);
        
        verify(tokenService, times(1)).usuarioFromToken(TOKEN, repository);
    }
    
    @Test
    void naoDeveAtualizarSeInvalidaOuRepetida() {
        RegistroForm form = Criar.despesaForm();
        form.setMes(2);
        try {
            controller.atualizar(TOKEN, form, 4L);
            fail();
        } catch(RegistroRepetidoException e) {
        }
        form.setAno(2021);
        form.setMes(2);
        form.setDia(30);
        try {
            controller.atualizar(TOKEN, form, 4L);
            fail();
        } catch(DateTimeException e) {
        }
        
        verify(tokenService, times(2)).usuarioFromToken(TOKEN, repository);
    }
    
    @Test
    void deveDeletarSeExiste() {
        Long id = 10L;
        var resposta = controller.deletar(TOKEN, id);
        helper.codigo404(resposta);
        assertEquals(5, usuario.getRegistros().getDespesas().size());
        
        id = 1L;
        resposta = controller.deletar(TOKEN, id);
        helper.codigo200(resposta);
        assertEquals(4, usuario.getRegistros().getDespesas().size());
        verify(tokenService, times(2)).usuarioFromToken(TOKEN, repository);
    }

    @BeforeEach
    void setUp() throws Exception {
        mockito = Mockito.mockitoSession()
           .initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();
        when(tokenService.usuarioFromToken(TOKEN, repository)).thenReturn(usuario);
        controller = new DespesaController(repository, tokenService);
        helper = new TesteHelper();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockito.finishMocking();
    }

}
