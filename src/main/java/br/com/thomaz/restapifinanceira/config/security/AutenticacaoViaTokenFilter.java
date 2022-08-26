package br.com.thomaz.restapifinanceira.config.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
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

        var token = getTokenFrom(request);
        boolean ehValido = service.ehValido(token);
        
        if (ehValido) {
            autenticaRequest(token);
        }
        
        filterChain.doFilter(request, response);
    }

    private void autenticaRequest(String token) {
        var usuario = repository.findById(service.getUserIdFrom(token)).get();
        var userPassAuthToken =
                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getPerfis());
        SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
    }

    private String getTokenFrom(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7, token.length());
    }

}
