package br.com.thomaz.restapifinanceira.endpoint;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thomaz.restapifinanceira.config.service.TokenService;
import br.com.thomaz.restapifinanceira.endpoint.dto.TokenDto;
import br.com.thomaz.restapifinanceira.endpoint.form.LoginForm;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    
    private AuthenticationManager authManager;
    private TokenService tokenService;
    
    @Autowired
    public AutenticacaoController(AuthenticationManager authManager, TokenService service) {
        this.authManager = authManager;
        this.tokenService = service;
    }

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) {
        
        UsernamePasswordAuthenticationToken userPassAuthToken = form.toUserPassAuthToken();
        
        try {
            Authentication authentication = authManager.authenticate(userPassAuthToken);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.ok()
                    .body(new TokenDto(token, "Bearer"));
            
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    
}
