package br.com.thomaz.restapifinanceira.config.config.security;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{
    
    private TokenService service;
    private UsuarioRepository repository;
    
    public AutenticacaoViaTokenFilter(TokenService service, UsuarioRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        String token = request.getHeader("Authorization");
        boolean ehValido = service.ehValido(token);
        
        if (ehValido) {
            autenticaRequest(token);
        }
        
        filterChain.doFilter(request, response);
    }

    private void autenticaRequest(String token) {
        var usuario = repository.findById(service.idFromToken(token)).get();
        var userPassAuthToken =
                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getPerfis());
        SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
    }

}
