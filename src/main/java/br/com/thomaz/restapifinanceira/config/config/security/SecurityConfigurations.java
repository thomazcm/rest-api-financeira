package br.com.thomaz.restapifinanceira.config.config.security;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.server.header.ContentTypeOptionsServerHttpHeadersWriter;
import br.com.thomaz.restapifinanceira.repository.UsuarioRepository;

@EnableWebSecurity
public class SecurityConfigurations {

    private AutenticacaoService autenticacaoService;
    private TokenService tokenService;
    private UsuarioRepository repository;

    @Autowired
    public SecurityConfigurations(AutenticacaoService autenticacaoService,
            TokenService tokenService, UsuarioRepository repository) {
        this.autenticacaoService = autenticacaoService;
        this.tokenService = tokenService;
        this.repository = repository;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http.authorizeRequests()
        .antMatchers(HttpMethod.POST, "/auth").permitAll()
        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .antMatchers(HttpMethod.POST, "/usuarios").permitAll()
        .anyRequest().authenticated()
        .and()
        .csrf().disable()
        .headers()
        .addHeaderWriter(new StaticHeadersWriter(responseHeaders()))
        .frameOptions().sameOrigin()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, repository),
                UsernamePasswordAuthenticationFilter.class)
        
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
    
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(autenticacaoService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    private List<Header> responseHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setAccessControlAllowOrigin("http://localhost:8080");
        responseHeaders.setAccessControlAllowCredentials(true);
        responseHeaders.setAccessControlAllowMethods(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS));
        responseHeaders.setAccessControlAllowHeaders(List.of("Origin", "Content-Type", "Accept"));
        
        List<Header> headers = new ArrayList<>();
        responseHeaders.forEach((h,v) -> headers.add(new Header(h, v.get(0))));
        
        return headers;
    }


}
