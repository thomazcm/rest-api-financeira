package br.com.thomaz.restapifinanceira.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collection = "usuarios")
public class Usuario implements UserDetails{
    
    private static final long serialVersionUID = -141479841793986041L;
    @Id
    private String id;
    private String nome;
    private String email;
    private String senha;
    private Collection<Perfil> perfis = new ArrayList<>();
    private Registros registros = new Registros();
    private Boolean ehUsuarioDemo;

    public Boolean ehDemo() {
        return ehUsuarioDemo;
    }

    public void setEhDemo(Boolean ehDemo) {
        this.ehUsuarioDemo = ehDemo;
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ehUsuarioDemo = false;
    }

    public Registros getRegistros() {
        return registros;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Collection<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(Collection<Perfil> perfis) {
        this.perfis = perfis;
    }

    public String getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(this.perfis);
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
