package br.com.thomaz.restapifinanceira.config.service;

import java.util.Date;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import br.com.thomaz.restapifinanceira.config.exception.UsuarioInexistenteException;
import br.com.thomaz.restapifinanceira.model.Usuario;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TokenService {
    
    @Value("${rest.jwt.expiration}")
    private String expiration;
    
    @Value("${rest.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {
        Usuario logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
        
        return Jwts.builder()
                .setIssuer("rest-api-financeira")
                .setSubject(logado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean ehValido(String tokenRaw) {
        String token = format(tokenRaw);
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                | SignatureException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public Usuario usuarioFromToken(String token, UsuarioRepository repo) {
        try {
            return repo.findById(idFromToken(token)).get();
        } catch (NoSuchElementException e) {
            throw new UsuarioInexistenteException();
        }
    }

    public String idFromToken(String tokenRaw) {
        String token = format(tokenRaw);
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private String format(String tokenRaw) {
        if (ObjectUtils.isEmpty(tokenRaw) || !tokenRaw.startsWith("Bearer ")) {
            return null;
        }
        String token = tokenRaw.substring(7, tokenRaw.length());
        System.out.println();
        return token;
    }

}
