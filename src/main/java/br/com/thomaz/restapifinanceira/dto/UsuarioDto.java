package br.com.thomaz.restapifinanceira.dto;

import br.com.thomaz.restapifinanceira.model.Usuario;

public class UsuarioDto {
    
    private String nome;
    private String email;

    public UsuarioDto(Usuario usuario) {
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
    
    

}
