package br.ce.alefecvo.servicos;

import br.ce.alefecvo.entidades.Usuario;

public interface EmailService {

    public void notificarAtraso(Usuario usuario);
}
