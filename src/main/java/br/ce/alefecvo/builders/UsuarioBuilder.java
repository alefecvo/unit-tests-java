package br.ce.alefecvo.builders;

import br.ce.alefecvo.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder(){
    }

    public static UsuarioBuilder umUsuario(){
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario();
        builder.usuario.setNome("Usuário 1");
        return builder;
    }

    public UsuarioBuilder nomeUsuario(String nome){
        this.usuario.setNome(nome);
        return this;
    }

    public Usuario agora(){
        return usuario;
    }
}
