package br.com.thomaz.restapifinanceira.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import br.com.thomaz.restapifinanceira.model.Usuario;

public class UsuarioForm {
    
    @NotBlank
    private String nome;
    @NotNull @Email
    private String email;
    @NotBlank @Length(min = 6, max = 20)
    private String senha;

    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        return usuario;
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

}
